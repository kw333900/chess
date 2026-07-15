package handler;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.RegisterRequest;
import service.RegisterResult;
import service.UserService;

public class UserHandler {


    public void handle_register(@NotNull Context context) {
        // create RegisterRequest object and then pass it into service method and call it


        var serializer = new Gson();


// deserialize
        RegisterRequest registerRequest = serializer.fromJson(context.body(), RegisterRequest.class);
        UserService userService = new UserService();

       try {
           RegisterResult registerResult = userService.register(registerRequest);
           var json = serializer.toJson(registerResult);
           context.result(json);
       } catch (AlreadyTakenException e){
           context.status(403);
           var json = serializer.toJson(e);
           context.result(json);
       }

    }

    public void handle_clear(@NotNull Context context){

    }








}
