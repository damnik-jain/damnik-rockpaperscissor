package com.hackerearth.fungame.controller;

import com.hackerearth.fungame.config.Constants;
import com.hackerearth.fungame.dao.DatabaseDAO;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GamePlay {

    //global variables
    static final HashMap<String, Integer> validMoves = new HashMap<>();
    static final HashMap<Integer, String> moveName = new HashMap<>();
    static final Map<Integer, Integer> winningMove = new HashMap<>();

    static {
        validMoves.put(Constants.ROCK,1);
        validMoves.put(Constants.PAPER,2);
        validMoves.put(Constants.SCISSOR,3);
        moveName.put(1,Constants.ROCK);
        moveName.put(2,Constants.PAPER);
        moveName.put(3,Constants.SCISSOR);
        winningMove.put(1,2);
        winningMove.put(2,3);
        winningMove.put(3,1);
    }

    @Autowired
    DatabaseDAO databaseDAO;

    //persist the moves in db and return response
    public JSONObject persistMoveAndReturnResponse(String token, int serverMove, String userSign) {
        JSONObject res = new JSONObject();

        //user input validation
        if (!validMoves.containsKey(userSign)){
            res.put("response","INVALID_MOVE_BY_USER");
            return res;
        }
        //converting user input string to int
        int userMove = validMoves.get(userSign);

        //to run the update query or not, do not run update in case of tie
        boolean update = false;
        if (userMove!=serverMove)
            update = true;

        //check if user won
        boolean userWon = checkIfUserWon(userMove, serverMove);

        //persist the result in h2 database and return response
        Map<String, Object> record = null;
        try {
            record = databaseDAO.updateAndGetScore(update, userWon, token);
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            //invalid token
            res.put("response","INVALID_TOKEN");
            return res;
        }catch (Exception e){
            //exception
            res.put("response","EXCEPTION");
            return res;
        }

        //game has ended
        if (record.get("status").equals("ENDED")){
            res.put("status","ENDED");
            return res;
        }

        //prepare response
        int uscore = (int) record.get("user_score");
        int sscore = (int) record.get("server_score");
        checkIfGameEnded(uscore,sscore,token,res);
        res.put("response",moveName.get(serverMove));
        res.put("totalScore","User "+uscore+" -  "+sscore+" Server");
        return res;

    }

    //checks if game has ended or not
    private void checkIfGameEnded(int uscore, int sscore, String token, JSONObject res) {
        if (uscore==3 || sscore==3){
            res.put("status","ENDED");
            databaseDAO.updateGameStatus(token,"ENDED");
            res.put("winner",uscore==3?"USER":"SERVER");
        }
    }


    //return true if user won else false
    private boolean checkIfUserWon(int userMove, int serverMove) {
        if (userMove==serverMove)
            return false;

        if (userMove==winningMove.get(serverMove))
            return true;
        else
            return false;
    }

}
