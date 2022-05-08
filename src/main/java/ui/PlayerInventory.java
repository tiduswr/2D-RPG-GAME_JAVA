package ui;

import interfaces.listeners.InventoryListener;

import java.awt.*;

import main.GamePanel;
import object.SuperObject;
import util.UtilityTool;

public class PlayerInventory implements InventoryListener{
    private Window window;
    private GamePanel gp;
    private int xOffset, yOffset, spaceBetween, COLS, ROWS;
    private InventorySlots slots;
    private ItemDescription desc;
    private Cursor cursor;
    private final int MAX_COLS = 5;
    
    public PlayerInventory(GamePanel gp, int x, int y, int spaceBetween, SuperObject[] itens) {
        //Basic configs
        this.gp = gp;
        xOffset = 20;
        yOffset = 20;
        this.spaceBetween = spaceBetween;
        System.out.println(y);
        
        //Inventory slots creation
        this.slots = new InventorySlots(x + xOffset, y + yOffset, itens, spaceBetween);
        
        //Window Creation
        COLS = MAX_COLS;
        int width = (gp.getTileSize() + spaceBetween)*COLS + (spaceBetween + xOffset);
        ROWS = (int) Math.ceil(itens.length/(float)COLS);
        int height = (gp.getTileSize() + spaceBetween)*ROWS + (spaceBetween + yOffset);
        this.window = new Window(x, y, width, height);
        System.out.println(y);
        
        //Cursor creation
        cursor = new Cursor(x + xOffset, y + yOffset, 
                gp.getTileSize(), gp.getTileSize(), spaceBetween,
                ROWS, COLS);
        
        //Description Window Creation
        y += height + spaceBetween;
        height = gp.getTileSize()*4;
        desc = new ItemDescription(x, y, width, height);
    }
    
    public void draw(Graphics2D g2){
        window.drawWindow(g2);
        slots.drawSlots(g2);
        cursor.draw(g2);
        desc.draw(g2);
    }
    
    public void cursorUp(){
        cursor.up();
    }

    public void cursorDown(){
        cursor.down();
    }

    public void cursorLeft(){
       cursor.left();
    }

    public void cursorRight(){
        cursor.right();
    }
    
    public SuperObject getSelectedItem(){
        return slots.getItemAt(cursor.getCurrentRow(), 
                                cursor.getCurrentCol());
    }
    
    public void updateInventory(SuperObject[] itens){
        slots.updateInventory(itens);
    }

    @Override
    public void update() {
        updateInventory(gp.getPlayer().getInventory());
    }
    
    private final class InventorySlots{
        
        protected int startX, startY, spaceBetween, ROWS;
        protected Slot slots[][];
        
        public InventorySlots(int startX, int startY, SuperObject[] itens, int spaceBetween) {
            this.startX = startX;
            this.startY = startY;
            this.spaceBetween = spaceBetween;
            updateInventory(itens);
        }
        
        public void updateInventory(SuperObject[] itens){
            int auxX = startX;
            int auxY = startY;
            
            ROWS = (int) (itens.length/(float) MAX_COLS);
            int slotI=0, slotJ=0;
            slots = new Slot[ROWS][MAX_COLS];
            for(int i = 1; i <= itens.length; i++){
                slots[slotI][slotJ] = new Slot(auxX, auxY, gp.getTileSize(), gp.getTileSize(), itens[i-1]);
                auxX += gp.getTileSize() + spaceBetween;
                slotJ++;
                if(i%MAX_COLS == 0) {
                    auxY += gp.getTileSize() + spaceBetween;
                    auxX = startX;
                    slotJ = 0;
                    slotI++;
                }
            }
        }
        
        public SuperObject getItemAt(int row, int col){
            if(!(row <= ROWS && row > 0 && 
               col <= MAX_COLS && col > 0)) return null;
            return slots[row-1][col-1].o;
        }
        
        public void drawSlots(Graphics2D g2){
            for(Slot arr[] : slots){
                for(Slot s : arr){
                    s.draw(g2);
                }
            }
        }
        
    }
    
    private class Slot{
        protected int x, y, width, height;
        protected SuperObject o;
        
        public Slot(int x, int y, int width, int height, SuperObject o) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.o = o;
        }
        
        public void draw(Graphics2D g2){
            if(o != null) {
                if(o == gp.getPlayer().getCurWeapon() || o == gp.getPlayer().getCurShield()){
                    Color oldColor = g2.getColor();

                    g2.setColor(Color.orange);
                    g2.fillRoundRect(x, y, width, height, 10, 10);

                    g2.setColor(oldColor);
                }
                g2.drawImage(o.getImage(), x, y, null);
            }

        }
        
    }
    
    private class Cursor{
        protected int x, y, startX, startY, row, col, rows, cols, width, height, spaceBetween;
        protected final Stroke CURSOR_STROKE = new BasicStroke(3);
        
        public Cursor(int x, int y, int width, int height, int spaceBetween, int rows, int cols) {
            this.x = x;
            startX = x;
            this.y = y;
            startY = y;
            
            this.rows = rows;
            this.cols = cols;
            row = 1;
            col = 1;
            
            this.width = width;
            this.height = height;
            this.spaceBetween = spaceBetween;
        }
        
        public int getCurrentRow(){
            return row;
        }
        
        public int getCurrentCol(){
            return col;
        }
        
        public void up(){
            if(row > 1) row--;
            update();
        }
        
        public void down(){
            if(row < rows) row++;
            update();
        }
        
        public void left(){
            if(col > 1) col--;
            update();
        }
        
        public void right(){
            if(col < cols) col++;
            update();
        }
        
        private void update(){
            x = (col-1)*gp.getTileSize() + (col-1)*spaceBetween + startX;
            y = (row-1)*gp.getTileSize() + (row-1)*spaceBetween + startY;
            gp.playSoundEffect("cursor", 0.3f);
        }
        
        public void draw(Graphics2D g2){
            Stroke oldCursor = g2.getStroke();
            
            g2.setStroke(CURSOR_STROKE);
            g2.drawRoundRect(x, y, width, height, 10, 10);
            
            g2.setStroke(oldCursor);
        }
        
    }
    
    private class ItemDescription{
        protected int x, y, width, height, xOffset = 20, yOffset = gp.getTileSize();
        private Window window;

        public ItemDescription(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.window = new Window(this.x, this.y, this.width, this.height);
        }
        
        public void draw(Graphics2D g2){
            Font oldFont = g2.getFont();

            //Draw Description
            SuperObject o = PlayerInventory.this.getSelectedItem();
            if(o != null){
                window.drawWindow(g2);
                g2.setFont(oldFont.deriveFont(Font.BOLD, oldFont.getSize()));
                g2.drawString("[" + o.getName() + "]", this.x + xOffset, this.y + yOffset);
                g2.setFont(oldFont.deriveFont(Font.PLAIN, oldFont.getSize()));
                UtilityTool.drawStringMultiLine(g2, o.getDescription(), this.width - xOffset,
                        this.x + xOffset, this.y + yOffset*2);
            }
            
            g2.setFont(oldFont);
        }
        
    }    
}
