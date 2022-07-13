#include <opencv2/opencv.hpp>
#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include "JNIDesktopBinding.h"

class ScreenShot
{
    Display* display;
    Window root;
    int x,y,width,height;
    XImage* img{nullptr};
public:
    ScreenShot(int x, int y, int width, int height):
        x(x),
        y(y),
        width(width),
        height(height)
    {
        display = XOpenDisplay(nullptr);
        root = DefaultRootWindow(display);
    }

    void operator() (cv::Mat& cvImg)
    {
        if(img != nullptr)
            XDestroyImage(img);
        img = XGetImage(display, root, x, y, width, height, AllPlanes, ZPixmap);
        cvImg = cv::Mat(height, width, CV_8UC4, img->data);
    }

    ~ScreenShot()
    {
        if(img != nullptr)
            XDestroyImage(img);
        XCloseDisplay(display);
    }
};
JNIEXPORT jdoubleArray JNICALL Java_JNIDesktopBinding_getAVG(JNIEnv *env, jobject){
    ScreenShot screen(1600,0,1920,1080);
    cv::Mat img;
    screen(img);
    cv::Scalar avg = cv::mean(img);
    double color[4] = {avg(0),avg(1),avg(2),avg(3)};
    jdoubleArray dArr = env->NewDoubleArray(4);
    env->SetDoubleArrayRegion(dArr,0 , 3, (jdouble*)color);
    return dArr;
}
