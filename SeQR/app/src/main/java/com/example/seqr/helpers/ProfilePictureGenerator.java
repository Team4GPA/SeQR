package com.example.seqr.helpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Helper class for generating profile pictures based on user information.
 */
public class ProfilePictureGenerator {
    private Paint paint;
    Paint PaintText;
    private Bitmap Picture;
    private Canvas canvas;

    /**
     * Constructor initializing necessary objects and settings for profile picture generation.
     */
    public ProfilePictureGenerator() {
        paint = new Paint();
        PaintText = new Paint();
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Picture = Bitmap.createBitmap(512,512,conf);
        canvas = new Canvas(Picture);
    }

    /**
     * Generates a profile picture based on user ID and user name.
     *
     * @param usr_id The user ID used to determine aspects of the profile picture.
     * @param user_name The user name to be displayed on the profile picture.
     * @return The generated profile picture as a Bitmap object.
     */
    public Bitmap generate(String usr_id, String user_name){
        String vc; //vertice color

        //first six letters of usr_id will determine background color
        String hexcode = usr_id.substring(0,6);
        hexcode = '#'+hexcode;
        canvas.drawColor(Color.parseColor(hexcode));

        //if user name is shorter than 3 characters: add characters till it is of length 3
        if (user_name.length() < 3){
            while (user_name.length() < 3){
                user_name = user_name + "*";
            }
        }

        char determiner = usr_id.charAt(9);

        if (Character.isDigit(determiner)){ //if 10th character is a number then pic will have letter in it
            //draw a circle with color based on 5th and 6th character of userid
            vc = "#"+usr_id.substring(6,8)+"0000";
            paint.setColor(Color.parseColor(vc));
            canvas.drawCircle(256,256, 200, paint);

            //draw a oval with fixed horizontal length and vertical length determined by userid
            vc = "#"+usr_id.substring(10,13)+usr_id.substring(14,17);
            paint.setColor(Color.parseColor(vc));
            int top = usr_id.charAt(17) - '0' + 1;
            top = top%10;
            top = top*100;
            int bottom = usr_id.charAt(18) - '0' + 1;
            bottom = bottom%10;
            bottom = bottom*100;
            canvas.drawOval(50,top,450,bottom,paint);

            //draw a oval with fixed vertical length and vertical length determined by userid
            vc = "#"+usr_id.substring(19,22)+"789";
            paint.setColor(Color.parseColor(vc));
            int left = usr_id.charAt(23) - '0' + 1;
            left = left%10;
            left = left;
            int right = usr_id.charAt(24) - '0' + 1;
            right = right%10;
            right = right*90;
            canvas.drawOval(left,300,right,500,paint);

            //draw first letter of username
            int temp = usr_id.charAt(25) - '0' + 1;
            temp = temp%10;
            vc = "#00"+ String.valueOf(temp) + "000";
            paint.setColor(Color.parseColor(vc));
            PaintText.setTextSize(260);
            PaintText.setColor(Color.parseColor(vc));
            canvas.drawText(user_name,0,1,126,326,PaintText);

            //draw second letter of username
            temp = usr_id.charAt(26) - '0' + 1;
            temp = temp%10;
            vc = "#"+ String.valueOf(temp) + "06020";
            paint.setColor(Color.parseColor(vc));
            PaintText.setTextSize(160);
            PaintText.setColor(Color.parseColor(vc));
            canvas.drawText(user_name,1,2,268,240,PaintText);

            //draw third letter of username
            temp = usr_id.charAt(27) - '0' + 1;
            temp = temp%10;
            vc = "#00"+ String.valueOf(temp) + "090";
            paint.setColor(Color.parseColor(vc));
            PaintText.setTextSize(160);
            PaintText.setColor(Color.parseColor(vc));
            canvas.drawText(user_name,2,3,268,320,PaintText);
        }
        else { //if 10th character is a letter then pic will have shapes
            //draw a triangle
            float[] vertices = {56, 429, 456, 429, 256, 83};
            vc = "#"+usr_id.substring(6,8)+"0000"; //use 5th and 6th character for color of triangle
            int vci = Color.parseColor(vc); //vertice color in int
            int verticesColors[] = {vci, vci, vci, vci, vci, vci};
            canvas.drawVertices(Canvas.VertexMode.TRIANGLES,6,vertices,0,null,0,
                    verticesColors,0,null,0,0,new Paint());

            //draw a rectangle with right and bottom side fixed and left and top side being determined by userid
            vc = "#"+usr_id.substring(10,13)+usr_id.substring(14,17);
            paint.setColor(Color.parseColor(vc));
            int left = usr_id.charAt(17) - '0' + 1;
            left = left%10;
            left = left*150;
            int top = usr_id.charAt(18) - '0' + 1;
            top = top%10;
            top = top*150;
            canvas.drawRect(left,top,250,250,paint);

            //draw rectangle with left and top side fixed and right and bottom side being determined by userid
            vc = "#"+usr_id.substring(19,22)+"789";
            paint.setColor(Color.parseColor(vc));
            int right = usr_id.charAt(23) - '0' + 1;
            right = right%10;
            right = right*150;
            int bottom = usr_id.charAt(24) - '0' + 1;
            bottom = bottom%10;
            bottom = bottom*90;
            canvas.drawRect(260,300,right,bottom,paint);

            //draw first letter of username
            int temp = usr_id.charAt(25) - '0' + 1;
            temp = temp%10;
            vc = "#00"+ String.valueOf(temp) + "000";
            paint.setColor(Color.parseColor(vc));
            PaintText.setTextSize(260);
            PaintText.setColor(Color.parseColor(vc));
            canvas.drawText(user_name,0,1,180,360,PaintText);
        }

        return Picture;
    }
}
