import pythoncom
import win32serviceutil
import win32service
import win32event
import servicemanager
import socket

class AppServerSvc(win32serviceutil.ServiceFramework):
    _svc_name_ = "EmotionService"
    _svc_display_name_ = "Servicio para detectar emociones por python"

    def __init__(self, args):
        win32serviceutil.ServiceFramework.__init__(self, args)
        self.hWaitStop = win32event.CreateEvent(None, 0, 0, None)
        socket.setdefaulttimeout(60)

    def SvcStop(self):
        self.ReportServiceStatus(win32service.SERVICE_STOP_PENDING)
        win32event.SetEvent(self.hWaitStop)

    def SvcDoRun(self):
        servicemanager.LogMsg(servicemanager.EVENTLOG_INFORMATION_TYPE,
                              servicemanager.PYS_SERVICE_STARTED,
                              (self._svc_name_,''))
        self.main()

    def main(self):
        import Pyro4
        import base64
        import numpy as np
        import cv2
        from detect_emotion import detect_emotion

        class emotion_service(object):
            def emotion_face_prediction(self, fotografia):
                img = base64.decodestring(fotografia["data"])
                img = np.asanyarray(bytearray(img))
                gray = cv2.imdecode(img, cv2.IMREAD_GRAYSCALE)
                result = detector.predict(gray)
                return result

        detector = detect_emotion("data/prueba.m", "data/X.x", "data/y.y")
        daemon = Pyro4.Daemon("localhost", 25312)
        uri = daemon.register(emotion_service, "emotion_service", True)
        print("Uri: ", uri)
        daemon.requestLoop()

if __name__ == "__main__":
    win32serviceutil.HandleCommandLine(AppServerSvc)