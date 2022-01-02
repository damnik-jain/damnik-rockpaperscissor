package com.hackerearth.fungame.controller;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2")
public class V2Controller {

    @Autowired
    GamePlay gamePlay;

    @RequestMapping("/{token}/{sign}")
    public @ResponseBody
    JSONObject serverPlay(@PathVariable(value="token") String token, @PathVariable(value="sign") String userSign) {

        // 1 - rock, 2 - paper, 3 - scissor
        int serverMove = 1; //default
        if (GamePlay.validMoves.containsKey(userSign))
            serverMove = GamePlay.winningMove.get(GamePlay.validMoves.get(userSign));

        return gamePlay.persistMoveAndReturnResponse(token, serverMove, userSign);
    }

}
