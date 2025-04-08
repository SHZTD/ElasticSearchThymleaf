package com.example.elastic.upload;

import com.example.elastic.logger.VerLogs;

public class Debugger {
    public static void main(String[] args) {
        SubirDatos sb = new SubirDatos();
        VerLogs vl = new VerLogs();
        sb.subir("access.log");
    }
}
