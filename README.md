#백엔드 서비스 아키텍처
<img width="2133" height="2313" alt="image" src="https://github.com/user-attachments/assets/dd5805ac-5cd1-40c2-b077-816e4b646c66" />


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

### 현재 진행 상황
#### 완료된 것
- guseok-drone (Python)
```
stream_server.py 완성
   - 웹캠/Tello 모드 전환 (MOCK_MODE)
   - MJPEG 스트리밍 (10fps)
   - AI 서버 전송 준비 (주석 처리)
   - Spring URL 등록 준비 (주석 처리)
requirements.txt
.env.example
.gitignore
```
- guseok-backend (Spring)
```
공통 응답 형식 (ApiResponse, ErrorCode)
GlobalExceptionHandler
SecurityConfig
WebSocketConfig
DroneStreamHandler
Drone Entity/Repository
DroneService
DroneController
   - POST /api/drone/stream-register
   - GET  /api/drone/status
   - GET  /api/drone/stream-url
   - DELETE /api/drone/{droneId}/disconnect
```

#### 앞으로 해야 할 것
- 1순위: 탐색 시작/종료 API
```
백엔드 팀원 Search Entity 확인 후
POST /api/drone/search/start/{searchId}
POST /api/drone/search/stop/{searchId}
```

- 2순위: AI 탐지 결과 수신 API
```
AI 팀원 서버에서 탐지 결과 보내면
Spring이 받아서 DB 저장

POST /api/ai/drone/result
```

- 3순위: WebSocket 브로드캐스트
```
AI 탐지 결과를 프론트로 실시간 전달

ws://서버/ws/drone/result/{searchId}
→ {"detections": [{"bbox": [...], "confidence": 0.92}]}
```

- 4순위: stream_server.py 연동 완성
```
Spring URL 등록 주석 해제
AI 서버 URL 연동 주석 해제
드론 도착 후 Tello 모드 테스트
```
