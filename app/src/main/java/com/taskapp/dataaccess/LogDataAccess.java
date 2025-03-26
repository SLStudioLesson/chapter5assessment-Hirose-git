package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.taskapp.model.Log;

public class LogDataAccess {
    private final String filePath;

    public LogDataAccess() {
        filePath = "app/src/main/resources/logs.csv";
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * 
     * @param filePath
     */
    public LogDataAccess(String filePath) {
        this.filePath = filePath;
    }

    /**
     * ログをCSVファイルに保存します。
     *
     * @param log 保存するログ
     */
    public void save(Log log) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = createLine(log);
            // 改行を追加する
            writer.newLine();
            // データを1行分追加する
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * すべてのログを取得します。
     *
     * @return すべてのログのリスト
     */
    public List<Log> findAll() {
        List<Log> logs = new ArrayList<Log>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // 先頭行を飛ばす
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length != 4) {
                    continue;
                }

                int taskCode = Integer.parseInt(values[0]);
                int changeUserCode = Integer.parseInt(values[1]);
                int status = Integer.parseInt(values[2]);
                LocalDate changeDate = LocalDate.parse(values[3]);

                Log log = new Log(taskCode, changeUserCode, status, changeDate);
                logs.add(log);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return logs;
    }

    /**
     * 指定したタスクコードに該当するログを削除します。
     *
     * @see #findAll()
     * @param taskCode 削除するログのタスクコード
     */
    public void deleteByTaskCode(int taskCode) {
        List<Log> logs = findAll();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // CSVのヘッダーを書き込む
            writer.write("Task_Code,Change_User_Code,Status,Change_Date");

            String line;
            for (Log log : logs) {
                // 削除対象のtaskなら、書き込みをスキップする
                if (log.getTaskCode() == taskCode) {
                    continue;
                } else {
                    line = createLine(log);
                }
                writer.newLine();
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ログをCSVファイルに書き込むためのフォーマットを作成します。
     *
     * @param log フォーマットを作成するログ
     * @return CSVファイルに書き込むためのフォーマット
     */
    private String createLine(Log log) {
        return log.getTaskCode() + "," + log.getChangeUserCode() + "," + log.getStatus() + "," + log.getChangeDate();
    }

}