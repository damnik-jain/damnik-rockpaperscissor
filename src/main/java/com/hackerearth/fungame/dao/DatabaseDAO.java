package com.hackerearth.fungame.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DatabaseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //creates the table and game instance
    public void createGameInstance(String token) {
        String createGamesTable = "CREATE TABLE IF NOT EXISTS games (\n" +
                "   token VARCHAR(50) NOT NULL,\n" +
                "   server_score INT NOT NULL DEFAULT 0,\n" +
                "   user_score INT NOT NULL DEFAULT 0,\n" +
                "   status VARCHAR(10) NOT NULL,\n" +
                "   creation_date TIMESTAMP,\n" +
                "   PRIMARY KEY (token) \n" +
                ")";
        jdbcTemplate.execute(createGamesTable);

        String insertQuery = "INSERT INTO games values (:token, 0, 0, :status, now())";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("token", token);
        paramMap.put("status", "READY");
        namedParameterJdbcTemplate.update(insertQuery, paramMap);
    }

    //persists the score in h2 db and returns the persisted value
    public Map<String, Object> updateAndGetScore(boolean update, boolean userWon, String token) {
        String selectQuery = "SELECT * from games where token=:token";

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("token", token);

        //run update query to increment user/server score
        if(update){
            String updateQuery = "UPDATE games SET server_score = server_score + 1 where token=:token and status!='ENDED'";
            if(userWon){
                updateQuery = "UPDATE games SET user_score = user_score + 1 where token=:token and status!='ENDED'";
            }
            int idUpdated = namedParameterJdbcTemplate.update(updateQuery, paramMap);
            System.out.println("idUpdated = " + idUpdated);
        }

        //return persisted value
        return namedParameterJdbcTemplate.queryForMap(selectQuery,paramMap);
    }

    //updates the game status
    public void updateGameStatus(String token, String status) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("token", token);
        paramMap.put("status", status);
        String updateQuery = "UPDATE games SET status = :status where token=:token";
        namedParameterJdbcTemplate.update(updateQuery, paramMap);
    }
}
