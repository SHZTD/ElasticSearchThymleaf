package com.example.elastic.controller;

import com.example.elastic.logger.VerLogs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin("*")
public class QueryController {

    private VerLogs verLogs = new VerLogs();

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String ip,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String urlContains,
            Model model) {

        StringBuilder query = new StringBuilder("FROM logs_server | KEEP ip, method, timestamp, url, status, size");

        List<String> conditions = new ArrayList<>();

        if (status != null && !status.isEmpty()) {
            conditions.add("status == " + status);
        }
        if (ip != null && !ip.isEmpty()) {
            conditions.add("ip == \"" + ip.replace("\"", "\\\"") + "\"");
        }
        if (method != null && !method.isEmpty()) {
            conditions.add("method == \"" + method.toUpperCase().replace("\"", "\\\"") + "\"");
        }
        if (urlContains != null && !urlContains.isEmpty()) {
            conditions.add("url CONTAINS \"" + urlContains.replace("\"", "\\\"") + "\"");
        }

        if (!conditions.isEmpty()) {
            query.append(" | WHERE ").append(String.join(" AND ", conditions));
        }

        query.append(" | LIMIT 100");

        List<List<String>> logs = verLogs.hacerQuery(query.toString());

        model.addAttribute("logs", logs);
        model.addAttribute("statusFilter", status != null ? status : "");
        model.addAttribute("ipFilter", ip != null ? ip : "");
        model.addAttribute("methodFilter", method != null ? method : "");
        model.addAttribute("urlContainsFilter", urlContains != null ? urlContains : "");

        return "index";
    }
}