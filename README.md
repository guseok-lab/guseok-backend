## 드론 영상 실시간 스트리밍 파이프라인 구현
### 전체 흐름
```
[Tello 드론]
    ↓ WiFi UDP
[맥북 Python - Flask MJPEG 서버]
    ↓ MJPEG HTTP
[AI 서버 Python]
    YOLO 탐지 + 바운딩박스 그리기
    ↓                    ↓
MJPEG 스트림         HTTP POST
탐지 영상             탐지 결과 JSON
    ↓                    ↓
[프론트 React]      [Spring AWS]
영상 표시                ↓
                    DB 저장
                    WebSocket
                        ↓
                   [프론트 React]
                   탐지 정보 표시
```
---
