# chat-with-stranger 채팅 프로젝트

## Introduction

![cws1](https://user-images.githubusercontent.com/35681772/68169503-cae6e500-ffaf-11e9-871d-3c61802e30b0.gif)

이 프로젝트는 WebSocket 과 Spring Boot MVC 프레임워크를 사용하여 구현한 채팅 프로젝트 입니다.

### WebSocket

![websocket](https://user-images.githubusercontent.com/35681772/64086002-b77d8a80-cd70-11e9-904f-7c4a22f8bfe9.png)

__웹 소켓(WebSocket)__ 은 서버와 클라이언트 간의 two-way communication channel 구축을 가능하게 하는 통신 프로토콜입니다. 또한, 웹 소켓은 TCP레이어 위에 존재하여 중간에 메세지를 전달할 수 있기 때문에 low-latency low-level communication 이 가능하며 전송되는 메세지 간의 오버헤드를 줄일 수 있습니다.

과거에는 클라이언트 단에서 Ajax 등을 사용하여 비동기로 반복 요청을 보내 웹 환경에서의 실시간 interactive application 을 제공했었습니다. 하지만 이는 결국 __폴링(Polling)__ 방식 즉, 데이터 수신을 위해 서버가 클라이언트에게 전송해 주는 푸시(push)방식이 아니라 클라이언트가 서버에에게 요청하는 폴링(polling) 방식이어서 그 한계가 존재했습니다.

__HTML5 가 도입__ 되며 순수 웹 환경에서 실시간 양방향 통신을 위한 스펙으로 '웹 소켓(Web Socket)' 이 제공되기 시작했습니다.

한 일본 사이트에서 웹 소켓과 Ajax의 통신 객체인 XMLHttpRequest 의 속도 비교를 해 본 결과 웹 소켓이 대략 50배 이상 좋은 성능을 보이고 있는것을 확인했습니다.

![websocket](https://user-images.githubusercontent.com/35681772/83391926-33efe780-a42f-11ea-9582-a458d49af2c4.gif)

> 출처 : https://m.mkexdev.net/98

웹 소켓 역시 일반적인 TCP 소켓 통신처럼 웹 소켓 역시 서버와 클라이언트간 데이터 교환이 이루어지는 형태입니다. 따라서 __클라이언트__ 에서는 웹 소켓이 제공하는 자바스크립트 API를 이용해 서버에 연결하고 데이터를 송/수신하는 코드를 구현하였고, __서버__ 에서는 Java API 가 제공하는 웹 소켓 프로토콜을 사용하여 구현했습니다.

## Project Description

### Login

<img width="800" alt="Screen Shot 2020-06-01 at 8 53 53 PM" src="https://user-images.githubusercontent.com/35681772/83406662-14b28380-a44a-11ea-90e0-1f90f8a38aad.png">

> 회원 가입에 사용된 컴포넌트 구조

채팅 방에 입장하기 위해선 로그인이 필요합니다. 그래서 로그인에 성공하면 세션을 발행해서 로그인 유무를 판단합니다.

### Chatroom

서버와 클라이언트간 데이터 통신의 프로토콜로는 JSON 을 사용했습니다. JSON 데이터와 메세지 객체간 보다 빠른 매핑을 위해 alibaba 에서 제공하는 [fastjson](https://github.com/alibaba/fastjson) 의 도움을 받았습니다.

JSON 에 보낼 데이터로 TYPE 을 명시하여 "ENTER", "CHAT", "LEAVE", "NOTICE", "RESOURCE" 항목으로 구분하여 각기 다른 처리가 프론트앤드 단에서 가능하도록 하였습니다.

사용자가 로그인 하여 세션이 발행되면, Platform 으로 Websocket Connection 을 수행합니다. 이 때, URL은 ```ws://{Host}:{Port}{Context}/open/{username}``` 로 지정하였습니다. 이 때 username 은 세션에서 정보를 가져옵니다.

세션의 유효성을 검증하여 검증되지 않은 접근에 있어 초기 화면으로 redirect 하는 방식으로 처리했습니다.

WebSocketServer 에서 사용한 메서드는 다음과 같습니다.

 * __sendMessageToAll__ : 모든 연결된 세션에 대해 broadcast 를 수행하는 메서드.
 * __onOpen__ : 클라이언트가 웹소켓에 들어오고 서버에 아무런 문제 없이 들어왔을 때 실행되는 메서드. Connection 이 수행되어 유효성 검증이 끝난 세션에 대해 username 을 Key 로, 세션을 Value 로 하여 서버 측 자료구조에 맵핑하여 정보를 저장합니다.
 * __onMessage__ : 클라이언트에게 메세지가 들어왔을 때 실행되는 메서드. 이 메세지를 바탕으로 sendMessageToAll 메서드를 실행하여 연결된 모든 세션에 broadcast 합니다.
 * __OnClose__ : 클라이언트와 웹소켓의 연결이 끊기면 실행되는 메서드. Connection 을 끊음으로써 서버 측 세션 맵에서 삭제를 수행합니다.
 * __onError__ : 예외를 다루는 메서드.

## 제공하는 기능
 * 오픈채팅방
   * 일반 채팅
   * 공지 기능
   * 파일 업로드 기능 - 업로드 파일 사이즈 조정 필요
 * 랜덤채팅방(예정)
   * 비동기 처리로 랜덤한 상대와 채팅방 연결(예정)

랜덤 채팅방에서의 흐름은 다음과 같습니다.

1. 채팅 요청 (Async : DeferredResult로 응답)
2. 유저 등록 (대기 큐에 등록)
3. 채팅 가능 체크 
 * 대기 큐에 2명 이상
   - UUID로 채팅방 이름 생성 + Success (+ 채팅방 이름 포함) 
   - UUID로 subscribe 요청
 * 대기 큐에 2명 미만이면 대기
   - 타임아웃 시 실패 처리


## 기술 스택
 * Web Socket
 * Spring & Spring boot
 * Spring JPA
 * MySQL
 * Javascript
 * Thymeleaf
 * HTML / CSS
 * Bootstrap

## Achievement

### 웹 소켓에 대한 이해

![web_socket](https://user-images.githubusercontent.com/35681772/83424491-6e757680-a467-11ea-85a1-661018b3c251.jpg)

> 출처 : https://www.tutorialspoint.com/websockets/websockets_quick_guide.htm

웹 소켓을 바탕으로 양방향 통신 특히, __클라이언트로 부터 요청이 들어오지 않았더라도 서버가 클라이언트로 메세지를 보낼 수 있는 점__ 이 어떤 가치를 제공하는지 이해하게 되었습니다.

### 비동기 처리

랜덤 채팅방에 있어서 채팅 요청 부터 채팅 형성까지는 비동기로 대기하고 있고, 채팅방이 형성되면 websocket으로 메시지를 주고받는 식으로 구현하기 위해 먼저 비동기 개념에 대해 공부했습니다.