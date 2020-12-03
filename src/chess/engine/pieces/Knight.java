package chess.engine.pieces;

import chess.engine.League;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static chess.engine.board.Move.*;

public final class Knight extends Piece{

    private final static int[] MOVE_VECTOR_COORDINATE = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final League pieceCOLOR, final int piecePosition) {
        super(PieceType.KNIGHT, piecePosition, pieceCOLOR, true);
    }

    public Knight(final League pieceCOLOR, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.BISHOP, piecePosition, pieceCOLOR, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOFFSET : MOVE_VECTOR_COORDINATE) {

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOFFSET;

            //not out of bound
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOFFSET) ||
                    isSecondColumnExclusion(this.piecePosition, currentCandidateOFFSET) ||
                    isSeventhColumnExclusion(this.piecePosition, currentCandidateOFFSET) ||
                    isEigthColumnExclusion(this.piecePosition, currentCandidateOFFSET)) {
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied() && this.isLegalMove(board, candidateDestinationCoordinate)) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));

                } else if (candidateDestinationTile.isTileOccupied()) {
                    final Piece pieceDestination = candidateDestinationTile.getPiece();
                    final League pieceCOLOR = pieceDestination.getLeague();

                    if (this.getLeague() != pieceCOLOR && this.isLegalMove(board, candidateDestinationCoordinate)) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                    }
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Knight movedPiece(Move move) {
        return new Knight(move.getMovedPiece().getLeague(), move.getDestinationCoordinate(), false);
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOFFSET == -17 || candidateOFFSET == -10 || candidateOFFSET == 6 || candidateOFFSET == 15);
    }
    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOFFSET == -10 || candidateOFFSET == 6);
    }
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOFFSET == -6 || candidateOFFSET == 10);
    }
    private static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOFFSET == -15 || candidateOFFSET == -6 || candidateOFFSET == 10 || candidateOFFSET == 17);
    }

}