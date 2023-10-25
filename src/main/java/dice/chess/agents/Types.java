package dice.chess.agents;

public class Types {
    
    public enum States {
        x0(0), x1(1), x2(2), x3(3), x4(4), x5(5), x6(6), x7(7),
        
        y0(0), y1(1), y2(2), y3(3), y4(4), y5(5), y6(6), y7(7),
        
        whitePawn(0), whiteRook(1), whiteKnight(2), whiteBishop(3), whiteQueen(4), whiteKing(5),
        
        blackPawn(6), blackRook(7), blackKnight(8), blackBishop(9), blackQueen(10), blackKing(11);
        
        public int value;
        
        States(int value) {
            
            this.value = value;
        }
    }
    
    public enum Actions {
    
        action0(new int[]{ 0, 0 }),
        action1(new int[]{ 0, 1 }),
        action2(new int[]{ 0, 2 }),
        action3(new int[]{ 0, 3 }),
        action4(new int[]{ 0, 4 }),
        action5(new int[]{ 0, 5 }),
        action6(new int[]{ 0, 6 }),
        action7(new int[]{ 0, 7 }),
        action8(new int[]{ 1, 0 }),
        action9(new int[]{ 1, 1 }),
        action10(new int[]{ 1, 2 }),
        action11(new int[]{ 1, 3 }),
        action12(new int[]{ 1, 4 }),
        action13(new int[]{ 1, 5 }),
        action14(new int[]{ 1, 6 }),
        action15(new int[]{ 1, 7 }),
        action16(new int[]{ 2, 0 }),
        action17(new int[]{ 2, 1 }),
        action18(new int[]{ 2, 2 }),
        action19(new int[]{ 2, 3 }),
        action20(new int[]{ 2, 4 }),
        action21(new int[]{ 2, 5 }),
        action22(new int[]{ 2, 6 }),
        action23(new int[]{ 2, 7 }),
        action24(new int[]{ 3, 0 }),
        action25(new int[]{ 3, 1 }),
        action26(new int[]{ 3, 2 }),
        action27(new int[]{ 3, 3 }),
        action28(new int[]{ 3, 4 }),
        action29(new int[]{ 3, 5 }),
        action30(new int[]{ 3, 6 }),
        action31(new int[]{ 3, 7 }),
        action32(new int[]{ 4, 0 }),
        action33(new int[]{ 4, 1 }),
        action34(new int[]{ 4, 2 }),
        action35(new int[]{ 4, 3 }),
        action36(new int[]{ 4, 4 }),
        action37(new int[]{ 4, 5 }),
        action38(new int[]{ 4, 6 }),
        action39(new int[]{ 4, 7 }),
        action40(new int[]{ 5, 0 }),
        action41(new int[]{ 5, 1 }),
        action42(new int[]{ 5, 2 }),
        action43(new int[]{ 5, 3 }),
        action44(new int[]{ 5, 4 }),
        action45(new int[]{ 5, 5 }),
        action46(new int[]{ 5, 6 }),
        action47(new int[]{ 5, 7 }),
        action48(new int[]{ 6, 0 }),
        action49(new int[]{ 6, 1 }),
        action50(new int[]{ 6, 2 }),
        action51(new int[]{ 6, 3 }),
        action52(new int[]{ 6, 4 }),
        action53(new int[]{ 6, 5 }),
        action54(new int[]{ 6, 6 }),
        action55(new int[]{ 6, 7 }),
        action56(new int[]{ 7, 0 }),
        action57(new int[]{ 7, 1 }),
        action58(new int[]{ 7, 2 }),
        action59(new int[]{ 7, 3 }),
        action60(new int[]{ 7, 4 }),
        action61(new int[]{ 7, 5 }),
        action62(new int[]{ 7, 6 }),
        action63(new int[]{ 7, 7 });
        
        public int[] value;
        
        Actions(int[] value) {
            
            this.value = value;
        }
    }
    
}
