package org.fasttrackit.todolist.persistance;

import org.fasttrackit.todolist.domain.TodoItem;
import org.fasttrackit.todolist.transfer.CreateToDoItemRequest;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToDoItemRepository {

    public void createToDoItem(CreateToDoItemRequest request) throws SQLException, IOException, ClassNotFoundException {
        String sql = "INSERT INTO to_do_item (description, deadline) VALUES(? ,?)";
        //try with resources
        try (Connection connection = DatabaseConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, request.getDescription());
            preparedStatement.setDate(2, Date.valueOf(request.getDeadline()));

            preparedStatement.executeUpdate();
        }

    }

    public void updateToDoItem(long id, boolean done) throws SQLException, IOException, ClassNotFoundException {
        String sql = "UPDATE to_do_item Set done=? Where id=?";

        try (Connection connection = DatabaseConfiguration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setBoolean(1, done);
            preparedStatement.setLong(2, id);

            preparedStatement.executeUpdate();

        }

    }

    public void deleteToDoItem(long id) throws SQLException, IOException, ClassNotFoundException {
        String sql = "DELETE FROM to_do_item where id=?";

        try (Connection connection = DatabaseConfiguration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1,id);

            preparedStatement.executeUpdate();
        }
    }

    public List<TodoItem> getToDoItems() throws SQLException, IOException, ClassNotFoundException {
        String sql = "SELECT id, description, deadline, done FROM to_do_item";

        List<TodoItem> todoItems = new ArrayList<>();

        try(Connection connection = DatabaseConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery(sql)){

            while(resultset.next()){
                TodoItem todoItem = new TodoItem();
                todoItem.setId(resultset.getLong("id"));
                todoItem.setDeadline(resultset.getDate("deadline").toLocalDate());
                todoItem.setDescription(resultset.getString("description"));
                todoItem.setDone(resultset.getBoolean("done"));

                todoItems.add(todoItem);
            }

        }

        return todoItems;
    }

}
