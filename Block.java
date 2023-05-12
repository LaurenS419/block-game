package assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;


public class Block {
    private int xCoord;
    private int yCoord;
    private int size; // height/width of the square
    private int level; // the root (outermost block) is at level 0
    private int maxDepth;
    private Color color;

    private Block[] children; // {UR, UL, LL, LR}


    public static Random gen = new Random();

    //dont
    public Block() {
    }

    public Block(int x, int y, int size, int lvl, int maxD, Color c, Block[] subBlocks) {
        this.xCoord = x; //
        this.yCoord = y; //
        this.size = size; //
        this.level = lvl;
        this.maxDepth = maxD;
        this.color = c;
        this.children = subBlocks;
    }
    // end


    /*
     * Creates a random block given its level and a max depth.
     *
     * xCoord, yCoord, size, and highlighted should not be initialized
     * (i.e. they will all be initialized by default)
     */
    public Block(int lvl, int maxDepth) {

        this.level = lvl;
        this.maxDepth = maxDepth;
        this.xCoord = 0;
        this.yCoord = 0;
        this.size = 0;
        this.children = new Block[]{};

        if(lvl > maxDepth){
            throw new IllegalArgumentException("level > maxDepth");
        }

        if (lvl < maxDepth && gen.nextDouble() < Math.exp(-0.25 * lvl)) {
            Block a = new Block(lvl + 1, maxDepth);
            Block b = new Block(lvl + 1, maxDepth);
            Block c = new Block(lvl + 1, maxDepth);
            Block d = new Block(lvl + 1, maxDepth);

            this.children = new Block[]{a, b, c, d};

        } else {
            int c = gen.nextInt(4);
            this.color = GameColors.BLOCK_COLORS[c];
        }
    }


    /*
     * Updates size and position for the block and all of its sub-blocks, while
     * ensuring consistency between the attributes and the relationship of the
     * blocks.
     *
     *  The size is the height and width of the block. (xCoord, yCoord) are the
     *  coordinates of the top left corner of the block.
     */
    public void updateSizeAndPosition(int size, int xCoord, int yCoord) {
        if(size <= 0 || xCoord < 0 || yCoord < 0){
            throw new IllegalArgumentException("Block dne");
        }
        if(size % 2 != 0 && size != 1){
            throw new IllegalArgumentException("size not even or not 1");
        }

        /*
        if((size % ((int) Math.pow(2, this.maxDepth))) % 2 != 0){
            throw new IllegalArgumentException("size wrong for maxDepth :eyes:");
        }
         */
        this.size = size;
        this.xCoord = xCoord;
        this.yCoord = yCoord;

        if(this.children.length != 0) {
            this.children[0].updateSizeAndPosition(this.size / 2, xCoord + (size / 2), yCoord);
            this.children[1].updateSizeAndPosition(this.size / 2, xCoord, yCoord);
            this.children[2].updateSizeAndPosition(this.size / 2, xCoord, yCoord + (size / 2));
            this.children[3].updateSizeAndPosition(this.size / 2, xCoord + (size / 2), yCoord + (size / 2));
        }
    }


    /*
     * Returns a List of blocks to be drawn to get a graphical representation of this block.
     *
     * This includes, for each undivided Block:
     * - one BlockToDraw in the color of the block
     * - another one in the FRAME_COLOR and stroke thickness 3
     *
     * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
     *
     * The order in which the blocks to draw appear in the list does NOT matter.
     */
    public ArrayList<BlockToDraw> getBlocksToDraw() {
        ArrayList<BlockToDraw> blocks = new ArrayList<BlockToDraw>();
        ArrayList<Block> temp = new ArrayList<Block>();

        //blocks.add(new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0));
        //blocks.add(new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord, this.yCoord, this.size, 3));

        getList(this, temp);
        //System.out.println(temp.size());

        for(Block bl: temp){
            blocks.add(new BlockToDraw(bl.color, bl.xCoord, bl.yCoord, bl.size, 0));
            blocks.add(new BlockToDraw(GameColors.FRAME_COLOR, bl.xCoord, bl.yCoord, bl.size, 3));
        }

        return blocks;
    }

    public void getList(Block cur, ArrayList<Block> blockList) {

        if (cur.children.length == 4) {

            getList(cur.children[0], blockList);
            getList(cur.children[1], blockList);
            getList(cur.children[2], blockList);
            getList(cur.children[3], blockList);
        } else {
            blockList.add(cur);
        }
    }

    /*
     * This method is provided and you should NOT modify it.
     */
    public BlockToDraw getHighlightedFrame() {
        return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
    }


    /*
     * Return the Block within this Block that includes the given location
     * and is at the given level. If the level specified is lower than
     * the lowest block at the specified location, then return the block
     * at the location with the closest level value.
     *
     * The location is specified by its (x, y) coordinates. The lvl indicates
     * the level of the desired Block. Note that if a Block includes the location
     * (x, y), and that Block is subdivided, then one of its sub-Blocks will
     * contain the location (x, y) too. This is why we need lvl to identify
     * which Block should be returned.
     *
     * Input validation:
     * - this.level <= lvl <= maxDepth (if not throw exception)
     * - if (x,y) is not within this Block, return null.
     */
    public Block getSelectedBlock(int x, int y, int lvl) {

        if(this.level > lvl || lvl > maxDepth){
            throw new IllegalArgumentException("this.level > lvl or lvl > maxDepth");
        }

        if(this.xCoord + this.size >= x && this.xCoord <= x && this.yCoord + this.size >= y && this.yCoord <= y){
            if(this.level == lvl || this.children.length == 0){ //or this.children.length == 0 ??
                return this;
            } else {
                if(this.xCoord <= x && this.xCoord+(this.size/2) >= x){
                    if(this.yCoord <= y && this.yCoord+(this.size/2) >= y){
                        //UL
                        return this.children[1].getSelectedBlock(x, y, lvl);
                    } else {
                        //LL
                        return this.children[2].getSelectedBlock(x, y, lvl);
                    }
                }
                else{
                    if(this.yCoord <= y && this.yCoord+(this.size/2) >= y){
                        //UR
                        return this.children[0].getSelectedBlock(x, y, lvl);
                    } else {
                        //LR
                        return this.children[3].getSelectedBlock(x, y, lvl);
                    }
                }
            }
        }
        return null;
    }


    /*
     * Swaps the child Blocks of this Block.
     * If input is 1, swap vertically. If 0, swap horizontally.
     * If this Block has no children, do nothing. The swap
     * should be propagate, effectively implementing a reflection
     * over the x-axis or over the y-axis.
     *
     */
    public void reflect(int direction) {

        if(this.size <= 0 || this.xCoord < 0 || this.yCoord < 0){
            throw new IllegalArgumentException("Block dne");
        }

        if(direction == 0){
            int cLine = this.yCoord + (this.size/2);
            for(Block bl: this.children) {
                reflOverX(bl, this.size, cLine);
            }

            if (this.children.length != 0){
                swapOverX(this);
            }

        } else if (direction == 1) {
            int cLine = this.xCoord + (this.size/2);
            for(Block bl: this.children) {
                reflOverY(bl, this.size, cLine);
            }
            if (this.children.length != 0) {
                swapOverY(this);
            }
        } else {
            throw new IllegalArgumentException("direction dne");
        }

    }

    private void reflOverX(Block block, int num, int cLine){

        int distFrom = cLine - block.yCoord;
        int newY = distFrom*2 + block.yCoord - block.size;
        block.yCoord = newY;

        if(block.children.length != 0){

            reflOverX(block.children[0],num, cLine);
            reflOverX(block.children[1],num, cLine);
            reflOverX(block.children[2],num, cLine);
            reflOverX(block.children[3],num, cLine);
            swapOverX(block);
        }
    }

    private void reflOverY(Block block,int num, int cLine){

        int distFrom = cLine - block.xCoord;
        int newX = distFrom*2 + block.xCoord - block.size;
        block.xCoord = newX;

        if(block.children.length != 0){

            reflOverY(block.children[0],num, cLine);
            reflOverY(block.children[1],num, cLine);
            reflOverY(block.children[2],num, cLine);
            reflOverY(block.children[3],num, cLine);
            swapOverY(block);
        }
    }

    private void swapOverX(Block block){

        //0 and 3 swap
        Block tmp1 = block.children[3];
        block.children[3] = block.children[0];
        block.children[0] = tmp1;

        //1 and 2 swap
        Block tmp2 = block.children[2];
        block.children[2] = block.children[1];
        block.children[1] = tmp2;

    }

    private void swapOverY(Block block){
        //0 and 1 swap
        Block tmp1 = block.children[1];
        block.children[1] = block.children[0];
        block.children[0] = tmp1;

        //3 and 2 swap
        Block tmp2 = block.children[2];
        block.children[2] = block.children[3];
        block.children[3] = tmp2;
    }


    /*
     * Rotate this Block and all its descendants.
     * If the input is 1, rotate clockwise. If 0, rotate
     * counterclockwise. If this Block has no children, do nothing.
     */
    public void rotate(int direction) {

        if(this.size <= 0 || this.xCoord < 0 || this.yCoord < 0){
            throw new IllegalArgumentException("Block dne");
        }

        if(direction == 1){
            rotCC(this);
            //swapRotCC(this);
        } else if (direction == 0) {
            rotC(this);
            //swapRotC(this);
        } else {
            throw new IllegalArgumentException("direction dne");
        }

    }

    private void rotCC(Block block){
        if(block.children.length != 0) {

            int p0x = block.children[0].xCoord;
            int p0y = block.children[0].yCoord;
            int p1x = block.children[1].xCoord;
            int p1y = block.children[1].yCoord;
            int p2x = block.children[2].xCoord;
            int p2y = block.children[2].yCoord;
            int p3x = block.children[3].xCoord;
            int p3y = block.children[3].yCoord;

            block.children[0].xCoord = p1x;
            block.children[0].yCoord = p1y;
            block.children[1].xCoord = p2x;
            block.children[1].yCoord = p2y;
            block.children[2].xCoord = p3x;
            block.children[2].yCoord = p3y;
            block.children[3].xCoord = p0x;
            block.children[3].yCoord = p0y;

            rotC(block.children[0]);
            rotC(block.children[1]);
            rotC(block.children[2]);
            rotC(block.children[3]);

            swapRotCC(block);
        }
    }

    private void rotC(Block block){

        if(block.children.length != 0){

            //System.out.println("0X " + block.children[0].xCoord + " Y " + block.children[0].yCoord);
            //System.out.println("1X " + block.children[1].xCoord + " Y " + block.children[1].yCoord);
            //System.out.println("2X " + block.children[2].xCoord + " Y " + block.children[2].yCoord);
            //System.out.println("3X " + block.children[3].xCoord + " Y " + block.children[3].yCoord);

            int p0x = block.children[0].xCoord;
            int p0y = block.children[0].yCoord;
            int p1x = block.children[1].xCoord;
            int p1y = block.children[1].yCoord;
            int p2x = block.children[2].xCoord;
            int p2y = block.children[2].yCoord;
            int p3x = block.children[3].xCoord;
            int p3y = block.children[3].yCoord;

            //System.out.println(p0x + " " + p0y);
            //System.out.println(p1x + " " + p1y);
            //System.out.println(p2x + " " + p2y);
            //System.out.println(p3x + " " + p3y);

            block.children[0].xCoord = p3x;
            block.children[0].yCoord = p3y;
            block.children[1].xCoord = p0x;
            block.children[1].yCoord = p0y;
            block.children[2].xCoord = p1x;
            block.children[2].yCoord = p1y;
            block.children[3].xCoord = p2x;
            block.children[3].yCoord = p2y;

            //System.out.println("0X " + block.children[0].xCoord + " Y " + block.children[0].yCoord);
            //System.out.println("1X " + block.children[1].xCoord + " Y " + block.children[1].yCoord);
            //System.out.println("2X " + block.children[2].xCoord + " Y " + block.children[2].yCoord);
            //System.out.println("3X " + block.children[3].xCoord + " Y " + block.children[3].yCoord);

            rotC(block.children[0]);
            rotC(block.children[1]);
            rotC(block.children[2]);
            rotC(block.children[3]);

            swapRotC(block);
        }
    }


    private void swapRotCC(Block block){

        Block[] temp = new Block[]{null, null, null, null};
        int j = 0;

        for(int i = 1; i < 4; i++){
            temp[j] = block.children[i];
            j++;
        }
        temp[3] = (block.children[0]);

        block.children = temp;
    }

    private void swapRotC(Block block){
        Block[] temp = new Block[]{null, null, null, null};
        int j = 0;

        for(int i = 1; i < 4; i++){
            temp[i] = block.children[j];
            j++;
        }
        temp[0] = (block.children[3]);

        block.children = temp;
    }


    /*
     * Smash this Block.
     *
     * If this Block can be smashed,
     * randomly generate four new children Blocks for it.
     * (If it already had children Blocks, discard them.)
     * Ensure that the invariants of the Blocks remain satisfied.
     *
     * A Block can be smashed iff it is not the top-level Block
     * and it is not already at the level of the maximum depth.
     *
     * Return True if this Block was smashed and False otherwise.
     *
     */
    public boolean smash() {

        if(this.level == 0 || this.level == this.maxDepth){
            return false;
        }

        Block[] kids = new Block[] {null, null, null, null};

        for(int i = 0; i < kids.length; i++){
            kids[i] = new Block(this.level + 1, this.maxDepth);
        }

        kids[0].updateSizeAndPosition(this.size/2, this.xCoord+(this.size/2), this.yCoord);
        kids[1].updateSizeAndPosition(this.size/2, this.xCoord, this.yCoord);
        kids[2].updateSizeAndPosition(this.size/2, this.xCoord, this.yCoord+(this.size/2));
        kids[3].updateSizeAndPosition(this.size/2, this.xCoord+(this.size/2), this.yCoord+(this.size/2));

        this.children = kids;

        return true;
    }


    /*
     * Return a two-dimensional array representing this Block as rows and columns of unit cells.
     *
     * Return and array arr where, arr[i] represents the unit cells in row i,
     * arr[i][j] is the color of unit cell in row i and column j.
     *
     * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
     */



    public Color[][] flatten() {
        Color[][] arr = new Color[][]{};

        arr = flat(arr, this);

        return arr;
    }

    public Color[][] flat(Color[][] arr, Block cur){

        if(cur.children.length != 4){

            Color[][] matrix = new Color[1][1];
            matrix[0][0] = cur.color;

            return matrix;

        } else {
            Color[][] m0 = flat(arr, cur.children[0]);
            Color[][] m1 = flat(arr, cur.children[1]);
            Color[][] m2 = flat(arr, cur.children[2]);
            Color[][] m3 = flat(arr, cur.children[3]);

            int s = findMaxS(m1.length, m2.length, m3.length, m0.length);
            s = s + s;
            Color[][] matrix = new Color[s][s];

            for(int row = 0; row < matrix.length; row++){
                for(int col = 0; col < matrix[0].length; col++){

                    if(col < s/2 && row < s/2){
                        if(m1.length > 1){
                            for(int i = 0; i < m1.length; i++){
                                for(int j = 0; j < m1[0].length; j++){
                                    matrix[i][j] = m1[i][j];
                                }
                            }
                        } else {
                            matrix[row][col] = m1[0][0];
                        }
                    } else if (col < s/2 && row >= s/2) {
                        if(m2.length > 1){
                            for(int i = 0; i < m2.length; i++){
                                for(int j = 0; j < m2[0].length; j++){
                                    matrix[i+(s/2)][j] = m2[i][j];
                                }
                            }
                        } else {
                            matrix[row][col] = m2[0][0];
                        }
                    } else if (col >= s/2 && row < s/2) {
                        if(m0.length > 1){
                            for(int i = 0; i < m0.length; i++){
                                for(int j = 0; j < m0[0].length; j++){
                                    matrix[i][j+(s/2)] = m0[i][j];
                                }
                            }
                        } else {
                            matrix[row][col] = m0[0][0];
                        }
                    } else {
                        if(m3.length > 1){
                            for(int i = 0; i < m3.length; i++){
                                for(int j = 0; j < m3[0].length; j++){
                                    matrix[i+(s/2)][j+(s/2)] = m3[i][j];
                                }
                            }
                        } else {
                            matrix[row][col] = m3[0][0];
                        }
                    }

                }
            }
            return matrix;

        }

    }

    private int findMaxS(int a, int b, int c, int d){
        int max = a;
        if(b > max){
            max = b;
        } if(c > max){
            max = c;
        } if(d > max){
            max = d;
        }

        return max;
    }


    // These two get methods have been provided. Do NOT modify them.
    public int getMaxDepth() {
        return this.maxDepth;
    }

    public int getLevel() {
        return this.level;
    }
    // -----

    /*
     * The next 5 methods are needed to get a text representation of a block.
     * You can use them for debugging. You can modify these methods if you wish.
     */
    public String toString() {
        return String.format("pos=(%d,%d), size=%d, level=%d"
                , this.xCoord, this.yCoord, this.size, this.level);
    }

    public void printBlock() {
        this.printBlockIndented(0);
    }

    private void printBlockIndented(int indentation) {
        //System.out.println(indentation);

        String indent = "";
        for (int i = 0; i < indentation; i++) {
            indent += "\t";
        }

        if (this.children.length == 0) {
            // it's a leaf. Print the color!
            String colorInfo = GameColors.colorToString(this.color) + ", ";
            System.out.println(indent + colorInfo + this);
        } else {
            System.out.println(indent + this);
            for (Block b : this.children)
                b.printBlockIndented(indentation + 1);
        }
    }

    private static void coloredPrint(String message, Color color) {
        System.out.print(GameColors.colorToANSIColor(color));
        System.out.print(message);
        System.out.print(GameColors.colorToANSIColor(Color.WHITE));
    }

    public void printColoredBlock() {
        Color[][] colorArray = this.flatten();
        for (Color[] colors : colorArray) {
            for (Color value : colors) {
                String colorName = GameColors.colorToString(value).toUpperCase();
                if (colorName.length() == 0) {
                    colorName = "\u2588";
                } else {
                    colorName = colorName.substring(0, 1);
                }
                coloredPrint(colorName, value);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        //perim 1
        Block[] children = new Block[4];

        children[0] = new Block(8, 0, 8, 1, 2, GameColors.GREEN, new Block[0]);
        children[1] = new Block(0, 0, 8, 1, 2, GameColors.BLUE, new Block[0]);
        children[2] = new Block(0, 8, 8, 1, 2, GameColors.RED, new Block[0]);
        children[3] = new Block(8, 8, 8, 1, 2, GameColors.YELLOW, new Block[0]);

        Block bp = new Block(0, 0, 16, 0, 2, null, children);
        bp.printColoredBlock();
        System.out.println(bp.children[0].children.length);

        System.out.println();

        //blob size

        //3 Yellow
        Block.gen = new Random(8);
        Block b3 = new Block(0, 2);
        b3.updateSizeAndPosition(16, 0, 0);

        b3.printColoredBlock();
        System.out.println();

        //blob scores

        //2 Red
        Block.gen = new Random(500);
        Block b2 = new Block(0, 3);
        b2.updateSizeAndPosition(16, 0, 0);

        b2.printColoredBlock();
        System.out.println();


    }

}
