/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hackerearth.fungame.controller;

import com.hackerearth.fungame.dao.DatabaseDAO;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@SpringBootApplication
public class GameController {

    @Autowired
    DatabaseDAO databaseDAO;

    @RequestMapping("/start")
    @ResponseBody
    JSONObject startGame() {
        JSONObject jsonObject = new JSONObject();
        String uuid = UUID.randomUUID().toString();
        databaseDAO.createGameInstance(uuid);
        jsonObject.put("token", uuid);
        jsonObject.put("status", "READY");
        return jsonObject;
    }

    @RequestMapping("/")
    @ResponseBody
    String indexPage() {
        return "Rock Paper Scissor Game";
    }

}
