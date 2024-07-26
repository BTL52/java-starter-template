package mino;

import app.GamePanel;
import app.KeyHandler;
import app.PlayManager;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Mino
{
    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1;
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactivateCounter = 0;

    public void create(Color c)
    {
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    public abstract void setXY(int x, int y);

    public void updateXY(int direction)
    {

        checkRotationCollsion();
        if (leftCollision == false && rightCollision == false && bottomCollision == false)
        {
            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }


    }

    // abstract method
    public abstract void getDirection1();


    public abstract void getDirection2();


    public abstract void getDirection3();


    public abstract void getDirection4();


    public void checkMovementCollsion()
    {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        for (int i = 0; i < b.length; i++)
        {
            if (b[i].x == PlayManager.left_x)
            {
                leftCollision = true;
            }
        }
        for (int i = 0; i < b.length; i++)
        {
            if (b[i].x + Block.SIZE == PlayManager.right_x)
            {
                rightCollision = true;
            }
        }
        for (int i = 0; i < b.length; i++)
        {
            if (b[i].y + Block.SIZE == PlayManager.bottom_y)
            {
                bottomCollision = true;
            }
        }
    }

    public void checkRotationCollsion()
    {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        for (int i = 0; i < tempB.length; i++)
        {
            if (tempB[i].x < PlayManager.left_x)
            {
                leftCollision = true;
            }
        }
        for (int i = 0; i < tempB.length; i++)
        {
            if (tempB[i].x + Block.SIZE > PlayManager.right_x)
            {
                rightCollision = true;
            }
        }
        for (int i = 0; i < tempB.length; i++)
        {
            if (tempB[i].y + Block.SIZE > PlayManager.bottom_y)
            {
                bottomCollision = true;
            }
        }
    }

    private void checkStaticBlockCollision()
    {
        for (int i = 0; i < PlayManager.staticBlocks.size(); i++)
        {
            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            for (int ii = 0; ii < b.length; ii++)
            {
                if (b[ii].y + Block.SIZE == targetY && b[ii].x == targetX)
                {
                    bottomCollision = true;
                }
            }
            for (int ii = 0; ii < b.length; ii++)
            {
                if (b[ii].x - Block.SIZE == targetX && b[ii].y == targetY)
                {
                    leftCollision = true;
                }
            }
            for (int ii = 0; ii < b.length; ii++)
            {
                if (b[ii].x + Block.SIZE == targetX && b[ii].y == targetY)
                {
                    rightCollision = true;
                }
            }
        }
    }

    public void update()
    {
        if (deactivating)
        {
            deactivating();
        }

        if (KeyHandler.upPressed)
        {
            switch (direction)
            {
                case 1:
                    getDirection2();
                    break;
                case 2:
                    getDirection3();
                    break;
                case 3:
                    getDirection4();
                    break;
                case 4:
                    getDirection1();
                    break;
            }
            KeyHandler.upPressed = false;
            GamePanel.se.play(3, false);
        }

        checkMovementCollsion();

        if (KeyHandler.downPressed)
        {
            if (bottomCollision == false)
            {

                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
            }
            else
            {
                GamePanel.se.play(4, false);
            }
            autoDropCounter = 0;
            KeyHandler.downPressed = false;
        }
        if (KeyHandler.leftPressed)
        {
            if (leftCollision == false)
            {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
                KeyHandler.leftPressed = false;
            }
        }
        if (KeyHandler.rightPressed)
        {
            if (rightCollision == false)
            {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
                KeyHandler.rightPressed = false;
            }
        }
        if (bottomCollision)
        {
            deactivating = true;
        }
        else
        {
            autoDropCounter++;
            if (autoDropCounter == PlayManager.dropInterval)
            {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
    }

    private void deactivating()
    {
        deactivateCounter++;
        if (deactivateCounter == 45)
        {
            deactivateCounter = 0;
            checkMovementCollsion();

            if (bottomCollision)
            {
                active = false;
            }
        }
    }

    public void draw(Graphics2D g2)
    {
        int margin = 2;
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x + margin, b[0].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[1].x + margin, b[1].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[2].x + margin, b[2].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[3].x + margin, b[3].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
    }
}
