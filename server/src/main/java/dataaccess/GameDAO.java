package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameDAO implements GameDAOinterface{
    final List<GameData> games = new ArrayList<>();
    int gameIDcounter=1;

    public void clear () {
        games.clear();
        gameIDcounter=1;
    }


    public int createGame (String gameName){
        games.add(new GameData(gameIDcounter, null, null, gameName, new ChessGame()));
        return gameIDcounter++;
    }



    public Collection<GameData> getGames (){
        return games;
    }


    public GameData getGameData (int gameID){
        for (GameData g : games){
            if (g.gameID() == gameID){
                return g;
            }
        }
        return null;
    }


    public void updateGameData(GameData gameData){
        int i=0;
        for (GameData g : games){
            if (g.gameID() == gameData.gameID()){
                games.set(i, gameData);
                break;
            }
            i++;
        }
    }
    

}
