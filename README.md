## 드론 영상 실시간 스트리밍 파이프라인 구현
### 전체 흐름
```
[DJI Tello]
    ↓ WiFi UDP (H.264)
[맥북 - Python]
    ↓ WebSocket
[AWS 백엔드 - Spring]
    ↓ HTTP POST
[AI 서버 - Python]
    ↓ WebSocket
[프론트엔드 - React]
```
---

### 단계별 요약
1. Tello → 맥북
- Tello WiFi에 맥북 접속
- djitellopy로 streamon 명령 전송
- UDP port 11111로 H.264 스트림 수신
- OpenCV로 디코딩 → JPEG 압축

2. 맥북 → AWS
- JPEG 프레임 + 드론 상태값(고도/배터리 등)을 WebSocket으로 전송

3. AWS → AI 서버
- Spring이 프레임 수신 후 AI 서버로 HTTP POST 전달

4. AI 서버 → AWS
- YOLOv8로 사람 탐지
- BotSort로 개인 ID 부여 (Re-ID)
- 결과 반환

5. AWS → 프론트
- 탐지 결과를 WebSocket으로 브로드캐스트
- React에서 탐지 인원/ID/신뢰도 실시간 표시

