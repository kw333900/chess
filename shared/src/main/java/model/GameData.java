package model;

import chess.*;

public record GameData(int gameID, String whiteUsername, String blackUsername, ChessGame game) {
}
