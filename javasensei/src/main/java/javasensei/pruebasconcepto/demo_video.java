package javasensei.pruebasconcepto;

import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import org.bytedeco.javacv.Frame;

public class demo_video {

    public static void main(String[] args) {
        //Create canvas frame for displaying webcam.
        CanvasFrame canvas = new CanvasFrame("Webcam");

        //Set Canvas frame to close on exit
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        //Declare FrameGrabber to import output from webcam
        FrameGrabber grabber = new OpenCVFrameGrabber("");        
        
        try {

            //Start grabber to capture video
            grabber.start();

            //Declare img as IplImage
            Frame img;
            
            while (true) {

                //inser grabed video fram to IplImage img
                img = grabber.grab();

                //Set canvas size as per dimentions of video frame.
                canvas.setCanvasSize(grabber.getImageWidth(), grabber.getImageHeight());
                
                if (img != null) {
                    //Flip image horizontally
                    //cvFlip(img.image, img.image, 1);
                    //Show video frame in canvas
                    canvas.showImage(img);                    
                }
            }
        } catch (Exception e) {            
        }
    }
}
