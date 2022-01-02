package com.hackerearth.fungame.controller;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/v1")
public class V1Controller {

    Random rand = new Random();
    int min = 1;
    int max = 3;

    @Autowired
    GamePlay gamePlay;

    public int randomMove(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    @RequestMapping("/{token}/{sign}")
    public @ResponseBody
    JSONObject serverPlay(@PathVariable(value="token") String token, @PathVariable(value="sign") String userSign) {

        // 1 - rock, 2 - paper, 3 - scissor
        int serverMove = randomMove(min, max);

        return gamePlay.persistMoveAndReturnResponse(token, serverMove, userSign);
    }


}
