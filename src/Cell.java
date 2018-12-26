public enum Cell {
    // states of 1x1 cell on board
    EMPTY, COLOR_ONE, COLOR_TWO, COLOR_ONE_MATCHED, COLOR_TWO_MATCHED;

    public Cell match(){
        switch (this){
            case COLOR_ONE: return COLOR_ONE_MATCHED;
            case COLOR_ONE_MATCHED: return COLOR_ONE_MATCHED;
            case COLOR_TWO: return COLOR_TWO_MATCHED;
            case COLOR_TWO_MATCHED: return COLOR_TWO_MATCHED;
            default: return this;
        }
    }
    public boolean isColorOne(){
        return this == COLOR_ONE || this == COLOR_ONE_MATCHED ;
    }
    public boolean isColorTwo(){
        return this == COLOR_TWO || this == COLOR_TWO_MATCHED;
    }
    public boolean isMatched(){
        return this == COLOR_ONE_MATCHED || this == COLOR_TWO_MATCHED;
    }
    public boolean equalColor(Cell cell){
        switch (this){
            case COLOR_ONE: return this == cell || cell == COLOR_ONE_MATCHED;
            case COLOR_TWO: return this == cell || cell == COLOR_TWO_MATCHED;
            case COLOR_ONE_MATCHED: return this == cell || cell == COLOR_ONE;
            case COLOR_TWO_MATCHED: return this == cell || cell == COLOR_TWO;
            default: return false;
        }
    }
}