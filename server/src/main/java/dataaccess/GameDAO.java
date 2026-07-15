package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.Collection;

public class GameDAO implements GameDAOinterface{
    final Collection<GameData> games = new ArrayList<>();


    public void clear () {
        games.clear();
    }


}
