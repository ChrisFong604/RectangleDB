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

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.sql.PreparedStatement;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String rectangle(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM rectangles");

      ArrayList<Rectangle> output = new ArrayList<Rectangle>();
      while (rs.next()) {
        Rectangle temp = new Rectangle();
        temp.setID(rs.getString("id"));
        temp.setName(rs.getString("name"));
        temp.setLength(rs.getInt("length"));
        temp.setWidth(rs.getInt("width"));
        temp.setColour(rs.getString("colour"));
        temp.setArea(rs.getInt("area"));

        output.add(temp);
      }
      model.put("rectangles", output);
      return "rectangle";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping(path = "/rectangle/create")
  public String getRectangleForm(Map<String, Object> model) {
    Rectangle rect = new Rectangle();
    model.put("rectangle", rect);

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM rectangles");

      ArrayList<Rectangle> output = new ArrayList<Rectangle>();
      while (rs.next()) {
        Rectangle temp = new Rectangle();
        temp.setID(rs.getString("id"));
        temp.setName(rs.getString("name"));
        temp.setLength(rs.getInt("length"));
        temp.setWidth(rs.getInt("width"));
        temp.setColour(rs.getString("colour"));
        temp.setArea(rs.getInt("area"));

        output.add(temp);
      }
      model.put("rectangles", output);
      return "rectangle";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping(path = "/rectangle/create", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String handleBrowserRectangleSubmit(Map<String, Object> model, Rectangle rect) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS rectangles (id serial, name varchar(20), width integer, length integer, colour varchar(20), area integer)");
      String sql = "INSERT INTO rectangles (name, width, length, colour, area) VALUES ('" + rect.getName() + "','"
          + rect.getWidth() + "','" + rect.getLength() + "','" + rect.getColour() + "','" + rect.getArea() + "')";
      stmt.executeUpdate(sql);
      return "redirect:/rectangle/success";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/rectangle/success")
  public String getRectangleSuccess(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM rectangles");

      ArrayList<Rectangle> output = new ArrayList<Rectangle>();
      while (rs.next()) {
        Rectangle temp = new Rectangle();
        temp.setID(rs.getString("id"));
        temp.setName(rs.getString("name"));
        temp.setLength(rs.getInt("length"));
        temp.setWidth(rs.getInt("width"));
        temp.setColour(rs.getString("colour"));
        temp.setArea(rs.getInt("area"));

        output.add(temp);
      }
      model.put("rectangles", output);
      return "rectanglesuccess";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/rectangle/delete")
  public String getRectangleToDelete(Map<String, Object> model, @RequestParam String rid) {
    try (Connection connection = dataSource.getConnection()) {
      // Statement stmt = connection.createStatement();
      // stmt.executeQuery("DELETE FROM rectangle WHERE id = {pid}");
      String sql = "DELETE FROM rectangles WHERE id = ?";
      PreparedStatement prepareStatement = connection.prepareStatement(sql);
      prepareStatement.setInt(1, Integer.parseInt(rid));
      prepareStatement.executeUpdate();
      // model.put("id", pid);
      return "rectangledelete";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/rectangle/read")
  public String getSpecificRectangle2(Map<String, Object> model, @RequestParam String rid) {
    try (Connection connection = dataSource.getConnection()) {
      // Statement stmt = connection.createStatement();
      // stmt.executeQuery("DELETE FROM rectangle WHERE id = {pid}");
      String sql = "SELECT * FROM rectangles WHERE id = ?";
      PreparedStatement prepareStatement = connection.prepareStatement(sql);
      prepareStatement.setInt(1, Integer.parseInt(rid));

      ResultSet rs = prepareStatement.executeQuery();

      Rectangle rect = new Rectangle();

      while (rs.next()) {
        rect.setID(rs.getString("id"));
        rect.setName(rs.getString("name"));
        rect.setLength(rs.getInt("length"));
        rect.setWidth(rs.getInt("width"));
        rect.setColour(rs.getString("colour"));
        rect.setArea(rs.getInt("area"));
      }

      model.put("record", rect);

      return "rectangleread";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }
}
