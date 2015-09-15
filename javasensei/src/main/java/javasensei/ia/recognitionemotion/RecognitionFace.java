package javasensei.ia.recognitionemotion;

/**
 *
 * @author Francisco Gonzalez Hernandez, Ramon Zatarain Cabada, Lucia Barron
 * Estrada, Raul Oramas Bustillos Email Contacto:
 * franciscogonzalez_hernandez@hotmail.com
 */
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc.CvFont;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;
import java.awt.image.BufferedImage;
import javasensei.util.FileHelper;
import static org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 *
 * @author Rock
 */
public class RecognitionFace {

    CvHaarClassifierCascade cascade_f;
    CvHaarClassifierCascade cascade_e_right;
    CvHaarClassifierCascade cascade_e2;
    CvHaarClassifierCascade cascade_m;

    CvMemStorage storage_cara;
    CvMemStorage storage_boca;
    CvMemStorage storage_ojo_der;
    CvMemStorage storage_ojo_izq;

    IplImage src;
    IplImage gray;
    IplImage grayThresh_hist_equa;
    IplImage gray_hist_equa;
    IplImage edges;

    int threshold_ojos = 80;
    int threshold_boca = 60;
    int threshold_ceja = 70;
    int maxValue = 255;
    int thresholdType = opencv_imgproc.CV_THRESH_BINARY;

    CvFont font;

    int lineaEnBlanco_contador = 0;
    int lineaEnBlanco_bandera = 0;
    int ojoDer_p1_flag = 0;
    int ojoDer_p4_flag = 0;
    int ojoIzq_p1_flag = 0;
    int ojoIzq_p4_flag = 0;
    int boca_p1_flag = 0;
    int boca_p2_flag = 0;
    int boca_p3_flag = 0;
    int boca_p4_flag = 0;
    int ceja_der_p1_flag = 0;
    int ceja_der_p3_flag = 0;
    int ceja_izq_p1_flag = 0;
    int ceja_izq_p2_flag = 0;
    int ojoDer_p1_X = 0;
    int ojoDer_p1_Y = 0;
    int ojoDer_p4_X = 0;
    int ojoDer_p4_Y = 0;
    int ojoIzq_p1_X = 0;
    int ojoIzq_p1_Y = 0;
    int ojoIzq_p4_X = 0;
    int ojoIzq_p4_Y = 0;
    int boca_p1_X = 0;
    int boca_p1_Y = 0;
    int boca_p2_X = 0;
    int boca_p2_Y = 0;
    int boca_p3_X = 0;
    int boca_p3_Y = 0;
    int boca_p4_X = 0;
    int boca_p4_Y = 0;
    int ceja_der_p1_X = 0;
    int ceja_der_p1_Y = 0;
    int ceja_der_p3_X = 0;
    int ceja_der_p3_Y = 0;
    int ceja_izq_p1_X = 0;
    int ceja_izq_p1_Y = 0;
    int ceja_izq_p2_X = 0;
    int ceja_izq_p2_Y = 0;
    //-------termina puntos X y Y
    // ----inicia distancias

    double ancho_cara = 0;

    //   double ojoDer_p1_p2 = 0;
    //  double ojoDer_p1_p3 = 0;
    //  double ojoDer_p2_p4 = 0;
    //   double ojoDer_p3_p4 = 0;
    double ojoDer_p1_p4 = 0;

//    double ojoIzq_p1_p2 = 0;
    //   double ojoIzq_p1_p3 = 0;
    //   double ojoIzq_p2_p4 = 0;
    //  double ojoIzq_p3_p4 =0;
    double ojoIzq_p1_p4 = 0;

    double boca_p1_p2 = 0;
    double boca_p1_p3 = 0;
    double boca_p2_p4 = 0;
    double boca_p3_p4 = 0;
    double boca_p1_p4 = 0;
    double boca_p2_p3 = 0;

    //   double ceja_der_p1_p2 = 0;
    double ceja_der_p1_p3 = 0;
    //   double ceja_der_p2_p3 = 0;

    double ceja_izq_p1_p2 = 0;
    //   double ceja_izq_p1_p3 = 0;
    //   double ceja_izq_p2_p3 = 0;

    public static CvHaarClassifierCascade obtenerClasificador(String file) throws Exception {
        CvHaarClassifierCascade clasificador = new CvHaarClassifierCascade(opencv_core.cvLoad(file));
        if (clasificador.isNull()) {
            throw new Exception("Error Loading Classifier File: " + file);
        }

        return clasificador;
    }

    static {
        Loader.load(opencv_objdetect.class);
    }

    /**
     *
     * @param image
     * @return @throws Exception
     */
    public RecognitionResult processFace(BufferedImage image) throws Exception {
        FileHelper helper = FileHelper.getInstance();

        // Archivos de cascada de caracteristicas para ...
        String file1 = helper.getFile("files/haarcascade_frontalface_alt2.xml"); // Deteccion de Rostros
        String file2 = helper.getFile("files/haarcascade_mcs_lefteye.xml"); // Deteteccion de Ojo derecho
        String file3 = helper.getFile("files/haarcascade_mcs_mouth.xml"); // Deteteccion de boca
        String file4 = helper.getFile("files/haarcascade_mcs_righteye.xml"); //para detectar ojo izquierdo

        cascade_f = obtenerClasificador(file1);
        cascade_e_right = obtenerClasificador(file2);
        cascade_m = obtenerClasificador(file3);
        cascade_e2 = obtenerClasificador(file4);

        storage_cara = CvMemStorage.create();
        storage_boca = CvMemStorage.create();
        storage_ojo_der = CvMemStorage.create();
        storage_ojo_izq = CvMemStorage.create();

        //IplImage.createFrom(image);
        IplImage frame =  new OpenCVFrameConverter.ToIplImage().convert(new Java2DFrameConverter().convert(image));
        
        RecognitionResult result = new RecognitionResult();

        result.setHayEmocion(detectEyes(frame));

        if (result.isHayEmocion()) {

            ojoDer_p1_p4 = distanciaPuntos(ojoDer_p1_X, ojoDer_p1_Y, ojoDer_p4_X, ojoDer_p4_Y);

            ojoIzq_p1_p4 = distanciaPuntos(ojoIzq_p1_X, ojoIzq_p1_Y, ojoIzq_p4_X, ojoIzq_p4_Y);

            boca_p1_p2 = distanciaPuntos(boca_p1_X, boca_p1_Y, boca_p2_X, boca_p2_Y);
            boca_p1_p3 = distanciaPuntos(boca_p1_X, boca_p1_Y, boca_p3_X, boca_p3_Y);
            boca_p2_p4 = distanciaPuntos(boca_p2_X, boca_p2_Y, boca_p4_X, boca_p4_Y);
            boca_p3_p4 = distanciaPuntos(boca_p3_X, boca_p3_Y, boca_p4_X, boca_p4_Y);
            boca_p1_p4 = distanciaPuntos(boca_p1_X, boca_p1_Y, boca_p4_X, boca_p4_Y);
            boca_p2_p3 = distanciaPuntos(boca_p2_X, boca_p2_Y, boca_p3_X, boca_p3_Y);

            ceja_der_p1_p3 = distanciaPuntos(ceja_der_p1_X, ceja_der_p1_Y, ceja_der_p3_X, ceja_der_p3_Y);

            ceja_izq_p1_p2 = distanciaPuntos(ceja_izq_p1_X, ceja_izq_p1_Y, ceja_izq_p2_X, ceja_izq_p2_Y);

            double[] values = new double[10];

            values[0] = ojoDer_p1_p4;
            values[1] = ojoIzq_p1_p4;
            values[2] = boca_p1_p2;
            values[3] = boca_p1_p3;
            values[4] = boca_p2_p4;
            values[5] = boca_p3_p4;
            values[6] = boca_p1_p4;
            values[7] = boca_p2_p3;
            values[8] = ceja_der_p1_p3;
            values[9] = ceja_izq_p1_p2;

            result.setCoordenadas(values);
        }

        //10 Coordenadas del rostro
        return result;
    }

    boolean detectEyes(IplImage img) {
        boolean result = false;

        boca_p1_flag = 0;
        boca_p2_flag = 0;
        boca_p3_flag = 0;
        boca_p4_flag = 0;
        ojoDer_p1_flag = 0;

        ojoDer_p4_flag = 0;
        ojoIzq_p1_flag = 0;

        ojoIzq_p4_flag = 0;
        ceja_der_p1_flag = 0;

        ceja_der_p3_flag = 0;
        ceja_izq_p1_flag = 0;
        ceja_izq_p2_flag = 0;

        //------------------CARA------------------
        CvSeq faces = opencv_objdetect.cvHaarDetectObjects(img, cascade_f, storage_cara,
                //1.1, 10, 0, cvSize( 40, 40 ) );
                1.1, 10, opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING);
        /* return if not found */
        if (faces.total() == 0) {
            return result;
        } else {
            result = true;
        }

        /* draw a rectangle */
        CvRect r = new CvRect(opencv_core.cvGetSeqElem(faces, 0));

        int rX = r.x();
        int rY = r.y();
        int rWidth = r.width();
        int rHeight = r.height();

        rX = (int) (rX + rWidth * 0.1);
        rY = (int) (rY + rHeight * 0.1);
        rWidth = (int) (rWidth * 0.8);

        ancho_cara = rWidth; // le damos el ancho de la cara

        opencv_imgproc.cvRectangle(img,
                opencv_core.cvPoint(rX, rY),
                opencv_core.cvPoint(rX + rWidth, rY + rHeight),
                opencv_core.CV_RGB(255, 0, 0), 1, 8, 0
        );

        opencv_core.cvSetImageROI(img, r);
        opencv_core.cvClearMemStorage(storage_cara);

//--------------cara--------------------
//-------------------BOCA---------------------------
        /* Set the Region of Interest: estimate the mouth' position */
        opencv_core.cvSetImageROI(img, opencv_core.cvRect(rX, rY + (int) (rHeight * 2 / 3.3), rWidth, rHeight / 3));
//este cuadro muestra el area donde se busca la boca

        /* detect mouth */
        CvSeq mouth = opencv_objdetect.cvHaarDetectObjects(img, cascade_m, storage_boca,
                //1.15, 6, 0, cvSize(25, 15));
                1.15, 6, opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING);

        /* draw a rectangle for each mouth found */
        CvRect r2;

        int r2X = 0;
        int r2Y = 0;
        int r2Width = 0;
        int r2Height = 0;

        if (mouth.total() > 0) {
            r2 = new CvRect(opencv_core.cvGetSeqElem(mouth, 0));

            r2X = r2.x();
            r2Y = r2.y();
            r2Width = r2.width();
            r2Height = r2.height();

            opencv_imgproc.cvRectangle(img,
                    opencv_core.cvPoint(r2X, r2Y),
                    opencv_core.cvPoint(r2X + r2Width, r2Y + r2Height),
                    opencv_core.CV_RGB(255, 255, 255), 1, 8, 0);
        }

        if (mouth.total() > 0) {

            opencv_core.cvSetImageROI(img, opencv_core.cvRect(rX + r2X, rY + r2Y + (int) (rHeight * 2 / 3.3), r2Width, r2Height));

            //	canvas_BocaSolo.showImage(img);
            //	canvas_BocaSolo.move(300, 0);			
            gray = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), opencv_core.IPL_DEPTH_8U, 1);
            opencv_imgproc.cvCvtColor(img, gray, opencv_imgproc.CV_BGR2GRAY);

            gray_hist_equa = opencv_core.cvCloneImage(gray);
            opencv_imgproc.cvEqualizeHist(gray, gray_hist_equa);
            //canvas_Boca_gray_hist_equa.showImage(gray_hist_equa);
            //canvas_Boca_gray_hist_equa.move(300, 100);
            grayThresh_hist_equa = opencv_core.cvCloneImage(gray_hist_equa);
            opencv_imgproc.cvThreshold(gray_hist_equa, grayThresh_hist_equa, threshold_boca, maxValue, thresholdType);

            //---------METODO 2  --- usa canny
//ya que tenemos la equalizacion de histograma (equaHist), le damos un pyrDown y pyrUp, para reducir a la mitad la imagen, esto para quitar un poco de ruido
// a la imagen ya con equaHist y con pyrdown y pyrUp, le hacemos un smooth gausiano
// y finalmente aplicamos el canny
            edges = opencv_core.cvCloneImage(gray_hist_equa);
            // para poder usar cvPyrDown, necesitamos que la imagen destino mida la mitad que la imagen source
            IplImage temp1;
            temp1 = opencv_core.cvCreateImage(opencv_core.cvSize(gray_hist_equa.width() / 2, gray_hist_equa.height() / 2), gray_hist_equa.depth(), gray_hist_equa.nChannels());
            opencv_imgproc.cvPyrDown(gray_hist_equa, temp1, opencv_imgproc.CV_GAUSSIAN_5x5);
            opencv_imgproc.cvPyrUp(temp1, edges, opencv_imgproc.CV_GAUSSIAN_5x5);

            opencv_imgproc.cvSmooth(edges, edges);

            opencv_imgproc.cvCanny(edges, edges, 30, 200, 3);

            CvMat mat_Boca = opencv_core.cvCreateMat(edges.height(), edges.width(), opencv_core.CV_8UC1);
            opencv_core.cvConvert(edges, mat_Boca);

//de P1 - P4, se cambiara todos los grayThresh_hist_equa por edges . y todos los scal.val[0] == 0 por scal.val[0] == 255
            //----------para boca P1
            for (int i = 1; i < edges.height(); i++) {
                for (int j = 1; j < edges.width(); j++) {
                    CvScalar scal = opencv_core.cvGet2D(mat_Boca, i, j);

                    if (scal.val(0) == 255
                            && boca_p1_flag == 0
                            && j > edges.width() * 0.45 // le decimos que busque desde la mitad de achura
                            && j < edges.width() * 0.65 // y que deje de buscar al llegar al 65% de la anchura
                            && i > edges.height() * 0.1 //finalmente, que busque a partir del 20% de alto, para evitar que agarre la ceja
                            ) {
                        boca_p1_X = j;
                        boca_p1_Y = i;
                        boca_p1_flag = 1;
                    }
                    if (boca_p1_flag == 1) {
                        break;
                    }
                }

                if (boca_p1_flag == 1) {
                    break;
                }
            }

            opencv_imgproc.cvCircle(edges, opencv_core.cvPoint(boca_p1_X, boca_p1_Y), 1, CvScalar.GREEN, 2, 8, 0);
            opencv_imgproc.cvCircle(img, opencv_core.cvPoint(boca_p1_X, boca_p1_Y), 1, CvScalar.GREEN, 2, 8, 0);

            //---------- TERMINA para boca P1
            //----------para boca P2
            for (int i = 4; i < edges.width() / 2; i++) {
                for (int j = 1; j < edges.height(); j++) {
                    CvScalar scal = opencv_core.cvGet2D(mat_Boca, i, j);

                    if (scal.val(0) == 255
                            && boca_p2_flag == 0
                            && j > edges.height() * 0.333 //
                            && j < edges.height() * 0.666 //
                            && i < edges.width() * 0.4 //
                            && i > edges.width() * 0.15 //
                            ) {
                        boca_p2_X = i;
                        boca_p2_Y = j;
                        boca_p2_flag = 1;
                    }
                    if (boca_p2_flag == 1) {
                        break;
                    }
                }

                if (boca_p2_flag == 1) {
                    break;
                }
            }
            opencv_imgproc.cvCircle(edges, opencv_core.cvPoint(boca_p2_X, boca_p2_Y), 1, CvScalar.RED, 2, 8, 0);
            opencv_imgproc.cvCircle(img, opencv_core.cvPoint(boca_p2_X, boca_p2_Y), 1, CvScalar.RED, 2, 8, 0);

            //---------- TERMINA para boca P2
            //----------para boca P3
            for (int i = edges.width() - 1; i > 2; i--) {
                for (int j = 1; j < edges.height(); j++) {
                    CvScalar scal = opencv_core.cvGet2D(mat_Boca, j, i);

                    if (scal.val(0) == 255
                            && boca_p3_flag == 0
                            && j > edges.height() * 0.333 //
                            && j < edges.height() * 0.666 //
                            && i > edges.width() * 0.5 //
                            ) {
                        boca_p3_X = i;
                        boca_p3_Y = j;
                        boca_p3_flag = 1;
                    }
                    if (boca_p3_flag == 1) {
                        break;
                    }
                }

                if (boca_p3_flag == 1) {
                    break;
                }
            }

            opencv_imgproc.cvCircle(edges, opencv_core.cvPoint(boca_p3_X, boca_p3_Y), 1, CvScalar.RED, 2, 8, 0);
            opencv_imgproc.cvCircle(img, opencv_core.cvPoint(boca_p3_X, boca_p3_Y), 1, CvScalar.RED, 2, 8, 0);

            //---------- TERMINA para boca P3
            //----------para boca P4
            for (int i = edges.height() - 1; i > 4; i--) {
                for (int j = 1; j < edges.width() - 1; j++) {
                    CvScalar scal = opencv_core.cvGet2D(mat_Boca, i, j);

                    if (scal.val(0) == 255
                            //   && lineaEnBlanco_bandera == 1
                            && boca_p4_flag == 0
                            && j > edges.width() * 0.5 // le decimos que busque desde la mitad de achura
                            && j < edges.width() * 0.65 // y que deje de buscar al llegar al 65% de la anchura
                            && i > edges.height() * 0.3
                            && i < edges.height() * 0.8) {
                        boca_p4_X = j;
                        boca_p4_Y = i;

                        boca_p4_flag = 1;
                    }
                    if (boca_p4_flag == 1) {
                        break;
                    }
                }

                if (boca_p4_flag == 1) {
                    break;
                }
            }

            opencv_imgproc.cvCircle(edges, opencv_core.cvPoint(boca_p4_X, boca_p4_Y), 1, CvScalar.YELLOW, 2, 8, 0);
            opencv_imgproc.cvCircle(img, opencv_core.cvPoint(boca_p4_X, boca_p4_Y), 1, CvScalar.YELLOW, 2, 8, 0);

            //---------- TERMINA boca P4
            //canvas_2Boca_grayThresh.showImage(edges);
            //canvas_2Boca_grayThresh.move(300, 300);
            if (boca_p1_flag == 1 && boca_p2_flag == 1) {
                opencv_imgproc.cvLine(img, opencv_core.cvPoint(boca_p1_X, boca_p1_Y), opencv_core.cvPoint(boca_p2_X, boca_p2_Y), CvScalar.YELLOW, 1, 8, 0);
            }
            if (boca_p1_flag == 1 && boca_p3_flag == 1) {
                opencv_imgproc.cvLine(img, opencv_core.cvPoint(boca_p1_X, boca_p1_Y), opencv_core.cvPoint(boca_p3_X, boca_p3_Y), CvScalar.YELLOW, 1, 8, 0);
            }
            if (boca_p1_flag == 1 && boca_p4_flag == 1) {
                opencv_imgproc.cvLine(img, opencv_core.cvPoint(boca_p1_X, boca_p1_Y), opencv_core.cvPoint(boca_p4_X, boca_p4_Y), CvScalar.YELLOW, 1, 8, 0);
            }
            if (boca_p2_flag == 1 && boca_p3_flag == 1) {
                opencv_imgproc.cvLine(img, opencv_core.cvPoint(boca_p2_X, boca_p2_Y), opencv_core.cvPoint(boca_p3_X, boca_p3_Y), CvScalar.YELLOW, 1, 8, 0);
            }
            if (boca_p2_flag == 1 && boca_p4_flag == 1) {
                opencv_imgproc.cvLine(img, opencv_core.cvPoint(boca_p2_X, boca_p2_Y), opencv_core.cvPoint(boca_p4_X, boca_p4_Y), CvScalar.YELLOW, 1, 8, 0);
            }
            if (boca_p3_flag == 1 && boca_p4_flag == 1) {
                opencv_imgproc.cvLine(img, opencv_core.cvPoint(boca_p3_X, boca_p3_Y), opencv_core.cvPoint(boca_p4_X, boca_p4_Y), CvScalar.YELLOW, 1, 8, 0);
            }

        }

        opencv_core.cvClearMemStorage(storage_boca);
        opencv_core.cvResetImageROI(img);

//-----------------------boca--------------------------
//--------------------OJO DERECHO-------------------------------

        /* Set the Region of Interest: estimate the eyes' position */
        opencv_core.cvSetImageROI(img, opencv_core.cvRect(rX, rY + (int) (rHeight / 5.5), (int) (rWidth / 1.5), (int) (rHeight / 3.0)));

        //   canvas_ROI_Ojos.showImage(img);
        //   canvas_ROI_Ojos.move(500, 580);

        /* detect eyes */
        CvSeq eyes = opencv_objdetect.cvHaarDetectObjects(img, cascade_e_right, storage_ojo_der,
                1.15, 3, 0);

        /* draw a rectangle for each eye found */
        CvRect r3;

        int r3X = 0;
        int r3Y = 0;
        int r3Width = 0;
        int r3Height = 0;

        if (eyes.total() > 0) {
            r3 = new CvRect(opencv_core.cvGetSeqElem(eyes, 0));

            r3X = r3.x();
            r3Y = r3.y();
            r3Width = r3.width();
            r3Height = r3.height();

            opencv_imgproc.cvRectangle(img,
                    opencv_core.cvPoint(r3X, r3Y),
                    opencv_core.cvPoint(r3X + r3Width, r3Y + r3Height),
                    opencv_core.CV_RGB(0, 0, 255), 1, 8, 0);
        }
        if (eyes.total() > 0) {
            opencv_core.cvSetImageROI(img, opencv_core.cvRect(rX + r3X, rY + r3Y + (int) (rHeight / 5.5), r3Width, r3Height));

            gray = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), opencv_core.IPL_DEPTH_8U, 1);
            opencv_imgproc.cvCvtColor(img, gray, opencv_imgproc.CV_BGR2GRAY);
            gray_hist_equa = opencv_core.cvCloneImage(gray);
            opencv_imgproc.cvEqualizeHist(gray, gray_hist_equa);
            //canvas_OJO_gray_hist_equa.showImage(gray_hist_equa);
            //canvas_OJO_gray_hist_equa.move(150, 100);
            grayThresh_hist_equa = opencv_core.cvCloneImage(gray_hist_equa);
            opencv_imgproc.cvThreshold(gray_hist_equa, grayThresh_hist_equa, threshold_ojos, maxValue, thresholdType);

            CvMat mat_Ojo_Der = opencv_core.cvCreateMat(grayThresh_hist_equa.height(), grayThresh_hist_equa.width(), opencv_core.CV_8UC1);
            opencv_core.cvConvert(grayThresh_hist_equa, mat_Ojo_Der);

            //----------para ojo derecho P1
            for (int i = 1; i < grayThresh_hist_equa.height(); i++) {
                for (int j = 1; j < grayThresh_hist_equa.width(); j++) {
                    CvScalar scal = opencv_core.cvGet2D(mat_Ojo_Der, i, j);
                    if (scal.val(0) == 255 && lineaEnBlanco_bandera == 0) // el 255 es para el blanco
                    {
                        lineaEnBlanco_contador = lineaEnBlanco_contador + 1;
                    }
                    if (lineaEnBlanco_contador >= grayThresh_hist_equa.width()) {
                        lineaEnBlanco_bandera = 1;
                    }

                    if (scal.val(0) == 0 && lineaEnBlanco_bandera == 0) // el 0 es para el negro
                    {
                        break;
                    }

                    if (scal.val(0) == 0 && lineaEnBlanco_bandera == 1 && ojoDer_p1_flag == 0
                            && j > grayThresh_hist_equa.width() * 0.5 // le decimos que busque desde la mitad de achura
                            && j < grayThresh_hist_equa.width() * 0.65 // y que deje de buscar al llegar al 65% de la anchura
                            && i > grayThresh_hist_equa.height() * 0.2 //finalmente, que busque a partir del 20% de alto, para evitar que agarre la ceja
                            ) {
                        ojoDer_p1_X = j;
                        ojoDer_p1_Y = i;
                        ojoDer_p1_flag = 1;
                    }
                    if (ojoDer_p1_flag == 1) {
                        break;
                    }
                }
                if (lineaEnBlanco_contador < grayThresh_hist_equa.width()) {
                    lineaEnBlanco_bandera = 0;
                }
                if (ojoDer_p1_flag == 1) {
                    break;
                }
            }

            opencv_imgproc.cvCircle(grayThresh_hist_equa, opencv_core.cvPoint(ojoDer_p1_X, ojoDer_p1_Y), 1, CvScalar.GREEN, 2, 8, 0);
            opencv_imgproc.cvCircle(img, opencv_core.cvPoint(ojoDer_p1_X, ojoDer_p1_Y), 1, CvScalar.GREEN, 2, 8, 0);
            // a continuacion reseteamos las variables ya usadas.
            lineaEnBlanco_bandera = 0;
            lineaEnBlanco_contador = 0;

	 //---------- TERMINA para ojo derecho P1
	/* //----------para ojo derecho P2

             //---------- TERMINA para ojo derecho P3
		  
             */
            //----------para ojo derecho P4
            for (int i = grayThresh_hist_equa.height() - 1; i > 4; i--) {
                for (int j = 1; j < grayThresh_hist_equa.width() - 1; j++) {
                    CvScalar scal = opencv_core.cvGet2D(mat_Ojo_Der, i, j);

                    if (scal.val(0) == 0
                            //   && lineaEnBlanco_bandera == 1
                            && ojoDer_p4_flag == 0
                            && j > grayThresh_hist_equa.width() * 0.5 // le decimos que busque desde la mitad de achura
                            && j < grayThresh_hist_equa.width() * 0.65 // y que deje de buscar al llegar al 65% de la anchura
                            && i > grayThresh_hist_equa.height() * 0.3
                            && i < grayThresh_hist_equa.height() * 0.8) {
                        ojoDer_p4_X = j;
                        ojoDer_p4_Y = i;
                        ojoDer_p4_flag = 1;
                    }
                    if (ojoDer_p4_flag == 1) {
                        break;
                    }
                }

                if (ojoDer_p4_flag == 1) {
                    break;
                }
            }
            opencv_imgproc.cvCircle(grayThresh_hist_equa, opencv_core.cvPoint(ojoDer_p4_X, ojoDer_p4_Y), 1, CvScalar.YELLOW, 2, 8, 0);
            opencv_imgproc.cvCircle(img, opencv_core.cvPoint(ojoDer_p4_X, ojoDer_p4_Y), 1, CvScalar.YELLOW, 2, 8, 0);

            //---------- TERMINA para ojo derecho P4
            //canvas_2OJO_grayThresh.showImage(grayThresh_hist_equa);
            //canvas_2OJO_grayThresh.move(150, 300);
            if (ojoDer_p1_flag == 1 && ojoDer_p4_flag == 1) {
                opencv_imgproc.cvLine(img, opencv_core.cvPoint(ojoDer_p1_X, ojoDer_p1_Y), opencv_core.cvPoint(ojoDer_p4_X, ojoDer_p4_Y), CvScalar.YELLOW, 1, 8, 0);
            }

        } // termina el if (eyes->total > 0)

        //  canvas_2OJO_grayThresh.showImage(grayThresh_hist_equa);
        // canvas_2OJO_grayThresh.move(150, 400);
        opencv_core.cvClearMemStorage(storage_ojo_der);
        opencv_core.cvResetImageROI(img);

//------------termina ojo derecho-------------------------------
//--------------------OJOS IZQUIERDO------------------------------

        /* Set the Region of Interest: estimate the eyes' position */
        opencv_core.cvSetImageROI(img, opencv_core.cvRect(rX + (int) (rWidth / 2.5), rY + (int) (rHeight / 5.5), (int) (rWidth / 1.7), (int) (rHeight / 3.0)));

        //      canvas_ROI_Ojos2.showImage(img);
        //     canvas_ROI_Ojos2.move(800, 580);
        /* detect eyes */
        CvSeq eyes2 = opencv_objdetect.cvHaarDetectObjects(
                img, cascade_e2, storage_ojo_izq,
                1.15, 3, 0);

        /* draw a rectangle for each eye found */
        CvRect r32;

        int r32X = 0;
        int r32Y = 0;
        int r32Width = 0;
        int r32Height = 0;

        if (eyes2.total() > 0) {
            r32 = new CvRect(opencv_core.cvGetSeqElem(eyes2, 0));

            r32X = r32.x();
            r32Y = r32.y();
            r32Width = r32.width();
            r32Height = r32.height();

            opencv_imgproc.cvRectangle(img,
                    opencv_core.cvPoint(r32X, r32Y),
                    opencv_core.cvPoint(r32X + r32Width, r32Y + r32Height),
                    opencv_core.CV_RGB(0, 0, 255), 1, 8, 0);
        }

        if (eyes2.total() > 0) {
            opencv_core.cvSetImageROI(img, opencv_core.cvRect(rX + (int) (rWidth / 2.5) + r32X, rY + r32Y + (int) (rHeight / 5.5), r32Width, r32Height));

            //     canvas_OJOSolo2.showImage(img);
            //    canvas_OJOSolo2.move(480, 0);
            gray = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), opencv_core.IPL_DEPTH_8U, 1);
            opencv_imgproc.cvCvtColor(img, gray, opencv_imgproc.CV_BGR2GRAY);
		//	grayThresh = opencv_core.cvCloneImage( gray );
            //	canvas_OJO_gray2.showImage(gray);
            //	canvas_OJO_gray2.move(480, 100);

            //	cvThreshold(gray, grayThresh, threshold_ojos, maxValue, thresholdType);
            //	canvas_OJO_grayThresh2.showImage(grayThresh);
            //	canvas_OJO_grayThresh2.move(480, 200);
            gray_hist_equa = opencv_core.cvCloneImage(gray);
            opencv_imgproc.cvEqualizeHist(gray, gray_hist_equa);
            //canvas_OJO_gray_hist_equa2.showImage(gray_hist_equa);
            //canvas_OJO_gray_hist_equa2.move(480, 100);

            grayThresh_hist_equa = opencv_core.cvCloneImage(gray_hist_equa);
            opencv_imgproc.cvThreshold(gray_hist_equa, grayThresh_hist_equa, threshold_ojos, maxValue, thresholdType);

            CvMat mat_Ojo_Izq = opencv_core.cvCreateMat(grayThresh_hist_equa.height(), grayThresh_hist_equa.width(), opencv_core.CV_8UC1);
            opencv_core.cvConvert(grayThresh_hist_equa, mat_Ojo_Izq);

            //----------para ojo izq P1
            for (int i = 1; i < grayThresh_hist_equa.height(); i++) {
                for (int j = 1; j < grayThresh_hist_equa.width(); j++) {
                    CvScalar scal = opencv_core.cvGet2D(mat_Ojo_Izq, i, j);
                    if (scal.val(0) == 255 && lineaEnBlanco_bandera == 0) // el 255 es para el blanco
                    {
                        lineaEnBlanco_contador = lineaEnBlanco_contador + 1;
                    }
                    if (lineaEnBlanco_contador >= grayThresh_hist_equa.width()) {
                        lineaEnBlanco_bandera = 1;
                    }

                    if (scal.val(0) == 0 && lineaEnBlanco_bandera == 0) // el 0 es para el negro
                    {
                        break;
                    }

                    if (scal.val(0) == 0 && lineaEnBlanco_bandera == 1 && ojoIzq_p1_flag == 0
                            && j > grayThresh_hist_equa.width() * 0.5 // le decimos que busque desde la mitad de achura
                            && j < grayThresh_hist_equa.width() * 0.65 // y que deje de buscar al llegar al 65% de la anchura
                            && i > grayThresh_hist_equa.height() * 0.2 //finalmente, que busque a partir del 20% de alto, para evitar que agarre la ceja
                            ) {
                        ojoIzq_p1_X = j;
                        ojoIzq_p1_Y = i;

                        ojoIzq_p1_flag = 1;
                    }
                    if (ojoIzq_p1_flag == 1) {
                        break;
                    }
                }
                if (lineaEnBlanco_contador < grayThresh_hist_equa.width()) {
                    lineaEnBlanco_bandera = 0;
                }
                if (ojoIzq_p1_flag == 1) {
                    break;
                }
            }

            opencv_imgproc.cvCircle(grayThresh_hist_equa, opencv_core.cvPoint(ojoIzq_p1_X, ojoIzq_p1_Y), 1, CvScalar.GREEN, 2, 8, 0);
            opencv_imgproc.cvCircle(img, opencv_core.cvPoint(ojoIzq_p1_X, ojoIzq_p1_Y), 1, CvScalar.GREEN, 2, 8, 0);

            lineaEnBlanco_bandera = 0;
            lineaEnBlanco_contador = 0;

    			 //---------- TERMINA para ojo izquierdo P1
    	/*		 //----------para ojo izquierdo P2
             for(int i=4;i<grayThresh_hist_equa.width()/2;i++)
             {
             for(int j=1;j<grayThresh_hist_equa.height();j++)
             {
             CvScalar scal = cvGet2D( mat_Ojo_Izq,i,j);

             if (scal.val(0) == 0
             && ojoIzq_p2_flag == 0
             && j > grayThresh_hist_equa.height() * 0.45 //
             && j < grayThresh_hist_equa.height() * 0.55 //
             && i < grayThresh_hist_equa.width() *0.4	  //
             )
             {
             ojoIzq_p2_X = i;
             ojoIzq_p2_Y = j;

             ojoIzq_p2_flag = 1;
             }
             if (ojoIzq_p2_flag == 1)
             {
             break;
             }
             }

             if (ojoIzq_p2_flag == 1)
             {
             break;
             }
             }

             opencv_imgproc.cvCircle(grayThresh_hist_equa, cvPoint(ojoIzq_p2_X,ojoIzq_p2_Y), 1, CvScalar.RED, 2,8,0);
             cvCircle(img, cvPoint(ojoIzq_p2_X,ojoIzq_p2_Y), 1, CvScalar.RED, 2,8,0);

             //---------- TERMINA para ojo izquierdo P2
    			  
             */
            /*			  //----------para ojo izquierdo P3
             for(int i=grayThresh_hist_equa.width()-1;i>2;i--)
             {
             for(int j=1;j<grayThresh_hist_equa.height();j++)
             {
             CvScalar scal = cvGet2D( mat_Ojo_Izq,j,i);


             if (scal.val(0) == 0

             && ojoIzq_p3_flag == 0
             && j > grayThresh_hist_equa.height() * 0.45 //
             && j < grayThresh_hist_equa.height() * 0.55 //
             && i > grayThresh_hist_equa.width() *0.6	  //
             )
             {
             ojoIzq_p3_X = i;
             ojoIzq_p3_Y = j;
             ojoIzq_p3_flag = 1;
             }
             if (ojoIzq_p3_flag == 1)
             {
             break;
             }
             }

             if (ojoIzq_p3_flag == 1)
             {
             break;
             }
             }

             cvCircle(grayThresh_hist_equa, cvPoint(ojoIzq_p3_X,ojoIzq_p3_Y), 1, CvScalar.RED, 2,8,0);
             cvCircle(img, cvPoint(ojoIzq_p3_X,ojoIzq_p3_Y), 1, CvScalar.RED, 2,8,0);

             //---------- TERMINA para ojo izquierdo P3    				
             */
            //----------para ojo izquierdo P4
            for (int i = grayThresh_hist_equa.height() - 1; i > 4; i--) {
                for (int j = 1; j < grayThresh_hist_equa.width() - 1; j++) {
                    CvScalar scal = opencv_core.cvGet2D(mat_Ojo_Izq, i, j);

                    if (scal.val(0) == 0
                            //   && lineaEnBlanco_bandera == 1
                            && ojoIzq_p4_flag == 0
                            && j > grayThresh_hist_equa.width() * 0.5 // le decimos que busque desde la mitad de achura
                            && j < grayThresh_hist_equa.width() * 0.65 // y que deje de buscar al llegar al 65% de la anchura
                            && i > grayThresh_hist_equa.height() * 0.3
                            && i < grayThresh_hist_equa.height() * 0.8) {
                        ojoIzq_p4_X = j;
                        ojoIzq_p4_Y = i;

                        ojoIzq_p4_flag = 1;
                    }
                    if (ojoIzq_p4_flag == 1) {
                        break;
                    }
                }

                if (ojoIzq_p4_flag == 1) {
                    break;
                }
            }

            opencv_imgproc.cvCircle(grayThresh_hist_equa, opencv_core.cvPoint(ojoIzq_p4_X, ojoIzq_p4_Y), 1, CvScalar.YELLOW, 2, 8, 0);
            opencv_imgproc.cvCircle(img, opencv_core.cvPoint(ojoIzq_p4_X, ojoIzq_p4_Y), 1, CvScalar.YELLOW, 2, 8, 0);

            //---------- TERMINA para ojo derecho P4
            //canvas_2OJO_grayThresh2.showImage(grayThresh_hist_equa);
            //canvas_2OJO_grayThresh2.move(480, 300);
            if (ojoIzq_p1_flag == 1 && ojoIzq_p4_flag == 1) {
                opencv_imgproc.cvLine(img, opencv_core.cvPoint(ojoIzq_p1_X, ojoIzq_p1_Y), opencv_core.cvPoint(ojoIzq_p4_X, ojoIzq_p4_Y), CvScalar.YELLOW, 1, 8, 0);
            }
        } // termina el if (eyes->total > 0)

        //	canvas_2OJO_grayThresh2.showImage(grayThresh_hist_equa);
        //	canvas_2OJO_grayThresh2.move(480, 400);
        opencv_core.cvClearMemStorage(storage_ojo_izq);
        opencv_core.cvResetImageROI(img);
        //-------------termina OJO izquierdo-------------------------------

//-----------ceja derecha------------
        opencv_core.cvSetImageROI(img, opencv_core.cvRect(rX + (int) (rWidth * 0.08), rY + (int) (rHeight * 0.06), (int) (rWidth * 0.38), (int) (rHeight * 0.23)));

        //  canvas_ROI_ceja_der_Solo.showImage(img);
        //  canvas_ROI_ceja_der_Solo.move(0, 0);  			 		   
        gray = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), opencv_core.IPL_DEPTH_8U, 1);
        opencv_imgproc.cvCvtColor(img, gray, opencv_imgproc.CV_BGR2GRAY);
        //  grayThresh = opencv_core.cvCloneImage( gray );
        //  canvas_ROI_ceja_der_gray.showImage(gray);
        //  canvas_ROI_ceja_der_gray.move(0, 100);				 			
        //  cvThreshold(gray, grayThresh, threshold_ceja, maxValue, thresholdType);
        //  canvas_ROI_ceja_der_grayThresh.showImage(grayThresh);
        //  canvas_ROI_ceja_der_grayThresh.move(0, 200);			 			
        gray_hist_equa = opencv_core.cvCloneImage(gray);
        opencv_imgproc.cvEqualizeHist(gray, gray_hist_equa);
        //canvas_ROI_ceja_der_gray_hist_equa.showImage(gray_hist_equa);
        //canvas_ROI_ceja_der_gray_hist_equa.move(0, 100);
        grayThresh_hist_equa = opencv_core.cvCloneImage(gray_hist_equa);
        opencv_imgproc.cvThreshold(gray_hist_equa, grayThresh_hist_equa, threshold_ceja, maxValue, thresholdType);

        //---------METODO 2  --- usa canny
        //ya que tenemos la equalizacion de histograma (equaHist), le damos un pyrDown y pyrUp, para reducir a la mitad la imagen, esto para quitar un poco de ruido
        // a la imagen ya con equaHist y con pyrdown y pyrUp, le hacemos un smooth gausiano
        // y finalmente aplicamos el canny
        edges = opencv_core.cvCloneImage(gray_hist_equa);
        // para poder usar cvPyrDown, necesitamos que la imagen destino mida la mitad que la imagen source
        IplImage temp1;
        temp1 = opencv_core.cvCreateImage(opencv_core.cvSize(gray_hist_equa.width() / 2, gray_hist_equa.height() / 2), gray_hist_equa.depth(), gray_hist_equa.nChannels());

        opencv_imgproc.cvPyrDown(gray_hist_equa, temp1, opencv_imgproc.CV_GAUSSIAN_5x5);
        opencv_imgproc.cvPyrUp(temp1, edges, opencv_imgproc.CV_GAUSSIAN_5x5);

        opencv_imgproc.cvSmooth(edges, edges);
        //canvas_CA_Ceja_Der_1.showImage(edges);
        //canvas_CA_Ceja_Der_1.move(0, 200);

        opencv_imgproc.cvCanny(edges, edges, 10, 100, 3);
		     				//cvShowImage( "CA_Boca_2", edges );
        //--------

        //  CvMat mat_ceja_der = cvCreateMat(grayThresh_hist_equa.height(),grayThresh_hist_equa.width(),CV_8UC1 );
        //  cvConvert( grayThresh_hist_equa, mat_ceja_der );
        CvMat mat_ceja_der = opencv_core.cvCreateMat(edges.height(), edges.width(), opencv_core.CV_8UC1);
        opencv_core.cvConvert(edges, mat_ceja_der);

        //de P1 - P4, se cambiara todos los grayThresh_hist_equa por edges . y todos los scal.val[0] == 0 por scal.val[0] == 255
        //----------para ceja der P1
        for (int i = 1; i < edges.height(); i++) {
            for (int j = 1; j < edges.width(); j++) {
                CvScalar scal = opencv_core.cvGet2D(mat_ceja_der, i, j);

                if (scal.val(0) == 255 && ceja_der_p1_flag == 0
                        && j > edges.width() * 0.5 // le decimos que busque desde la mitad de achura
                        && j < edges.width() * 0.65 // y que deje de buscar al llegar al 65% de la anchura
                        && i > edges.height() * 0.2 //finalmente, que busque a partir del 20% de alto, para evitar que agarre la ceja
                        ) {
                    ceja_der_p1_X = j;
                    ceja_der_p1_Y = i;

                    ceja_der_p1_flag = 1;
                }
                if (ceja_der_p1_flag == 1) {
                    break;
                }
            }

            if (ceja_der_p1_flag == 1) {
                break;
            }
        }
        opencv_imgproc.cvCircle(edges, opencv_core.cvPoint(ceja_der_p1_X, ceja_der_p1_Y), 1, CvScalar.GREEN, 2, 8, 0);
        opencv_imgproc.cvCircle(img, opencv_core.cvPoint(ceja_der_p1_X, ceja_der_p1_Y), 1, CvScalar.GREEN, 2, 8, 0);

	 //---------- TERMINA ceja der P1
/*	 //----------para ceja der P2
         for( int i=1;i<edges.height();i++)
         {
         for(int j=1;j<edges.width();j++)
         {
         CvScalar scal = cvGet2D( mat_ceja_der,i,j);

         if (scal.val(0) == 0
         && ceja_der_p2_flag == 0
         && j < edges.width() * 0.75 //
         && j > edges.width() * 0.18 //
         && i > edges.height() *0.4	  //
         && i < edges.height() *0.9	  //
         )
         {
         ceja_der_p2_X = j;
         ceja_der_p2_Y = i;

         ceja_der_p2_flag = 1;
         }
         if (ceja_der_p2_flag == 1)
         {
         break;
         }
         }

         if (ceja_der_p2_flag == 1)
         {
         break;
         }
         }

         cvCircle(edges, cvPoint(ceja_der_p2_X,ceja_der_p2_Y), 1, CvScalar.RED, 2,8,0);
         cvCircle(img, cvPoint(ceja_der_p2_X,ceja_der_p2_Y), 1, CvScalar.RED, 2,8,0);

         //---------- TERMINA para ceja der P2
	  
         */
        //----------para ceja der P3
        for (int i = 1; i < edges.height(); i++) {
            for (int j = edges.width() - 1; j > 2; j--) {
                CvScalar scal = opencv_core.cvGet2D(mat_ceja_der, i, j);

                if (scal.val(0) == 255
                        && ceja_der_p3_flag == 0
                        && i > edges.height() * 0.2 //
                        //  && j < grayThresh_hist_equa->height * 0.5 //
                        && j > edges.width() * 0.65 //
                        && j < edges.width() * 0.95) {
                    ceja_der_p3_X = j;
                    ceja_der_p3_Y = i;
                    ceja_der_p3_flag = 1;
                }
                if (ceja_der_p3_flag == 1) {
                    break;
                }
            }

            if (ceja_der_p3_flag == 1) {
                break;
            }
        }
        opencv_imgproc.cvCircle(edges, opencv_core.cvPoint(ceja_der_p3_X, ceja_der_p3_Y), 1, CvScalar.RED, 2, 8, 0);
        opencv_imgproc.cvCircle(img, opencv_core.cvPoint(ceja_der_p3_X, ceja_der_p3_Y), 1, CvScalar.RED, 2, 8, 0);

        //---------- TERMINA ceja der P3
        //canvas_2ROI_ceja_der_grayThresh.showImage(edges);
        //canvas_2ROI_ceja_der_grayThresh.move(0, 300);
        if (ceja_der_p1_flag == 1 && ceja_der_p3_flag == 1) {
            opencv_imgproc.cvLine(img, opencv_core.cvPoint(ceja_der_p1_X, ceja_der_p1_Y), opencv_core.cvPoint(ceja_der_p3_X, ceja_der_p3_Y), CvScalar.YELLOW, 1, 8, 0);
        }

        opencv_core.cvResetImageROI(img);

//-------fin ceja derecha---------
//-----------ceja izquierda------------
        opencv_core.cvSetImageROI(img, opencv_core.cvRect(rX + (int) (rWidth * 0.55), rY + (int) (rHeight * 0.06), (int) (rWidth * 0.35), (int) (rHeight * 0.23)));

        // canvas_ROI_ceja_izq_Solo.showImage(img);
        // canvas_ROI_ceja_izq_Solo.move(x, y);				
        gray = opencv_core.cvCreateImage(opencv_core.cvGetSize(img), opencv_core.IPL_DEPTH_8U, 1);
        opencv_imgproc.cvCvtColor(img, gray, opencv_imgproc.CV_BGR2GRAY);
        // grayThresh = opencv_core.cvCloneImage( gray );
        // canvas_ROI_ceja_izq_gray.showImage(gray);
        // canvas_ROI_ceja_izq_gray.move(640,100);	
        // cvThreshold(gray, grayThresh, threshold_ceja, maxValue, thresholdType);
        // canvas_ROI_ceja_izq_grayThresh.showImage(grayThresh);
        // canvas_ROI_ceja_izq_grayThresh.move(640, 200);
        gray_hist_equa = opencv_core.cvCloneImage(gray);
        opencv_imgproc.cvEqualizeHist(gray, gray_hist_equa);
        //canvas_ROI_ceja_izq_gray_hist_equa.showImage(gray_hist_equa);
        //canvas_ROI_ceja_izq_gray_hist_equa.move(640, 100);
        grayThresh_hist_equa = opencv_core.cvCloneImage(gray_hist_equa);
        opencv_imgproc.cvThreshold(gray_hist_equa, grayThresh_hist_equa, threshold_ceja, maxValue, thresholdType);

        //---------METODO 2  --- usa canny
        //ya que tenemos la equalizacion de histograma (equaHist), le damos un pyrDown y pyrUp, para reducir a la mitad la imagen, esto para quitar un poco de ruido
        // a la imagen ya con equaHist y con pyrdown y pyrUp, le hacemos un smooth gausiano
        // y finalmente aplicamos el canny
        edges = opencv_core.cvCloneImage(gray_hist_equa);
        // para poder usar cvPyrDown, necesitamos que la imagen destino mida la mitad que la imagen source
        //	IplImage* temp1;		
        temp1 = opencv_core.cvCreateImage(opencv_core.cvSize(gray_hist_equa.width() / 2, gray_hist_equa.height() / 2), gray_hist_equa.depth(), gray_hist_equa.nChannels());
        opencv_imgproc.cvPyrDown(gray_hist_equa, temp1, opencv_imgproc.CV_GAUSSIAN_5x5);
        opencv_imgproc.cvPyrUp(temp1, edges, opencv_imgproc.CV_GAUSSIAN_5x5);

        opencv_imgproc.cvSmooth(edges, edges);
        //canvas_CA_Ceja_Izq_1.showImage(edges);
        //canvas_CA_Ceja_Izq_1.move(640, 200);

        opencv_imgproc.cvCanny(edges, edges, 10, 100, 3);
		//cvShowImage( "CA_Boca_2", edges );
        //--------	

        //		CvMat mat_ceja_izq = cvCreateMat(grayThresh_hist_equa.height(),grayThresh_hist_equa.width(),CV_8UC1 );
        //		cvConvert( grayThresh_hist_equa, mat_ceja_izq );
        CvMat mat_ceja_izq = opencv_core.cvCreateMat(edges.height(), edges.width(), opencv_core.CV_8UC1);
        opencv_core.cvConvert(edges, mat_ceja_izq);

        //de P1 - P4, se cambiara todos los grayThresh_hist_equa por edges . y todos los scal.val[0] == 0 por scal.val[0] == 255
        //----------para ceja der P1
        for (int i = 1; i < edges.height(); i++) {
            for (int j = 1; j < edges.width(); j++) {
                CvScalar scal = opencv_core.cvGet2D(mat_ceja_izq, i, j);
                if (scal.val(0) == 255 && ceja_izq_p1_flag == 0
                        && j > edges.width() * 0.5 // le decimos que busque desde la mitad de achura
                        && j < edges.width() * 0.65 // y que deje de buscar al llegar al 65% de la anchura
                        && i > edges.height() * 0.2 //finalmente, que busque a partir del 20% de alto, para evitar que agarre la ceja
                        ) {
                    ceja_izq_p1_X = j;
                    ceja_izq_p1_Y = i;

                    ceja_izq_p1_flag = 1;
                }
                if (ceja_izq_p1_flag == 1) {
                    break;
                }
            }

            if (ceja_izq_p1_flag == 1) {
                break;
            }
        }
        opencv_imgproc.cvCircle(edges, opencv_core.cvPoint(ceja_izq_p1_X, ceja_izq_p1_Y), 1, CvScalar.GREEN, 2, 8, 0);
        opencv_imgproc.cvCircle(img, opencv_core.cvPoint(ceja_izq_p1_X, ceja_izq_p1_Y), 1, CvScalar.GREEN, 2, 8, 0);

        //---------- TERMINA ceja izq P1
        //----------para ceja izq P2
        for (int i = 1; i < edges.height(); i++) {
            for (int j = 1; j < edges.width(); j++) {
                CvScalar scal = opencv_core.cvGet2D(mat_ceja_izq, i, j);

                if (scal.val(0) == 255
                        && ceja_izq_p2_flag == 0
                        && j < edges.width() * 0.75 //
                        && j > edges.width() * 0.18 //
                        && i > edges.height() * 0.4 //
                        && i < edges.height() * 0.9 //
                        ) {
                    ceja_izq_p2_X = j;
                    ceja_izq_p2_Y = i;

                    ceja_izq_p2_flag = 1;
                }
                if (ceja_izq_p2_flag == 1) {
                    break;
                }
            }

            if (ceja_izq_p2_flag == 1) {
                break;
            }
        }

        opencv_imgproc.cvCircle(edges, opencv_core.cvPoint(ceja_izq_p2_X, ceja_izq_p2_Y), 1, CvScalar.RED, 2, 8, 0);
        opencv_imgproc.cvCircle(img, opencv_core.cvPoint(ceja_izq_p2_X, ceja_izq_p2_Y), 1, CvScalar.RED, 2, 8, 0);

        //---------- TERMINA para ceja izq P2
				/*		  //----------para ceja izq P3


         for(int i=1;i<grayThresh_hist_equa.height();i++)
         {
         for(int j=grayThresh_hist_equa.width()-1;j>2;j--)
         {
         CvScalar scal = cvGet2D( mat_ceja_izq,i,j);

         if (scal.val(0) == 0

         && ceja_izq_p3_flag == 0
         && i > grayThresh_hist_equa.height() * 0.2 //
         && j < grayThresh_hist_equa.width() * 0.8 //
         && j > grayThresh_hist_equa.width() *0.65	  //
         )
         {
         ceja_izq_p3_X = j;
         ceja_izq_p3_Y = i;
         ceja_izq_p3_flag = 1;
         }
         if (ceja_izq_p3_flag == 1)
         {
         break;
         }
         }

         if (ceja_izq_p3_flag == 1)
         {
         break;
         }
         }
         cvCircle(grayThresh_hist_equa, cvPoint(ceja_izq_p3_X,ceja_izq_p3_Y), 1, CvScalar.RED, 2,8,0);
         cvCircle(img, cvPoint(ceja_izq_p3_X,ceja_izq_p3_Y), 1, CvScalar.RED, 2,8,0);


         //---------- TERMINA ceja izq P3
						  
         */
        //canvas_2ROI_ceja_izq_grayThresh.showImage(edges);
        //canvas_2ROI_ceja_izq_grayThresh.move(640, 300);
        if (ceja_izq_p1_flag == 1 && ceja_izq_p2_flag == 1) {
            opencv_imgproc.cvLine(img, opencv_core.cvPoint(ceja_izq_p1_X, ceja_izq_p1_Y), opencv_core.cvPoint(ceja_izq_p2_X, ceja_izq_p2_Y), CvScalar.YELLOW, 1, 8, 0);
        }

        opencv_core.cvResetImageROI(img);

        return result;
//-------fin ceja izquierda---------
    }

    double distanciaPuntos(int p1X, int p1Y, int p2X, int p2Y) {
        double valorRetorno;
        double Tempo;

        if (p1X == p2X) {
            Tempo = p2Y - p1Y;
            if (Tempo < 0) {
                Tempo = p1Y - p2Y;
            }
            valorRetorno = Tempo / ancho_cara;
            return valorRetorno;
        }

        if (p1Y == p2Y) {
            Tempo = p2X - p2Y;
            if (Tempo < 0) {
                Tempo = p1X - p2X;
            }
            valorRetorno = Tempo / ancho_cara;
            return valorRetorno;
        }

        //en caso de que no haya iguales, tenemos que crear un tercer punto de referencia (el punto de union de los dos catetos) ,
        //para luego sacar con el teorema de pitagoras la hipotenusa, este punto lo logramos intercalando (X1,Y2) o con (X2,Y1)
        // cualquiera de esos deberia funcionar
        int pTemp3X;
        int pTemp3Y;
        double distCatetoHipotenusa1;
        double distCatetoHipotenusa2;

        pTemp3X = p1X;
        pTemp3Y = p2Y;

        distCatetoHipotenusa1 = p1Y - pTemp3Y;
        if (distCatetoHipotenusa1 < 0) {
            distCatetoHipotenusa1 = pTemp3Y - p1Y;

        }

        distCatetoHipotenusa2 = p2X - pTemp3X;
        if (distCatetoHipotenusa2 < 0) {
            distCatetoHipotenusa2 = pTemp3X - p2X;

        }

        //ahora si aplicamos teorema de pitagoras
        Tempo = Math.sqrt((distCatetoHipotenusa1 * distCatetoHipotenusa1) + (distCatetoHipotenusa2 * distCatetoHipotenusa2));

        valorRetorno = Tempo / ancho_cara;
        return valorRetorno;

    }

}
