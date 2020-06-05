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

### STOMP 프로토콜

랜덤 채팅방을 만들 때는 STOMP 프로토콜을 사용하려고 했습니다.

__STOMP__ 는 Simple/Streaming Text Oriented Messaging Protocol의 약자이며, 텍스트 기반의 메세징 프로토콜입니다. HTTP에 모델링된 frame 기반 프로토콜이므로 다음과 같은 Command 들을 지원합니다.

#### STOMP Commands
 * CONNECT
 * SEND
 * SUBSCRIBE
 * UNSUBSCRIBE
 * BEGIN
 * COMMIT
 * ABORT
 * ACK
 * NACK
 * DISCONNECT

#### STOMP 구성

 <img width="237" alt="Screen Shot 2020-06-02 at 5 03 24 PM" src="https://user-images.githubusercontent.com/35681772/83495712-0a988f80-a4f3-11ea-9357-deafd270d232.png">

 * Command -> String
 * Headers -> JavaScript Object
 * Body -> String

#### STOMP 구조

<img width="665" alt="Screen Shot 2020-06-02 at 5 06 19 PM" src="https://user-images.githubusercontent.com/35681772/83496003-682cdc00-a4f3-11ea-8ecb-b934aa78627d.png">

STOMP는 구독이라는 개념을 통해 내가 통신하고자 하는 주체(topic)를 판단하여 브로커라는 개념을 두어 실시간, 지속적으로 관심을 가지며 해당 요청이 들어오면 처리하게 됩니다. 이 과정은 MessageHandler를 구현하여 처리합니다.

이 내용은 [Spring Guide](https://spring.io/guides/gs/messaging-stomp-websocket/) 를 바탕으로 공부 및 작성했습니다.

### Troubleshooting

파일 전송에 있어 대부분의 사진과 같은 파일은 잘 전송이 되었지만, 영화와 같은 4GB 이상의 __대용량 파일 전송__ 에 있어 업로드가 안되는 상황이 발생하였습니다.

<img width="1160" alt="Screen Shot 2020-06-05 at 7 10 20 PM" src="https://user-images.githubusercontent.com/35681772/83864868-5439e200-a760-11ea-8112-f40f773094b0.png">

OutOfMemoryError 유형으로는 다음과 같은 것들이 있었습니다.

1. Java heap space
   - 말 그대로 자바 힙이 일시적인 과도한 요구 또는 지속적인 누수로 인해 더 이상 요청한 메모리를 할당할 수 없을 때 발생합니다. 특정 프로그램에서 한번에 많은 메모리를 할당하는 경우라면 -Xmx 옵션으로 힙 크기를 늘려서 해결할 수 있으나, 지속적 누수로 인한 경우라면 heap dump를 떠서 누수 포인트를 찾아야 합니다.
   - 일부 업로드 프로그램에서 메모리에다 전송된 파일을 전부 올리는 경우가 있는데, 파일에다 쓰는 방식으로 변경하거나 업로드 최대 용량을 제한하는 방식으로 개선해야 합니다.
   - heap dump 받은 후 분석을 필요로 합니다.
 
2. PermGen space
   - JVM 메모리는 young gen, old gen, permanent gen으로 나뉘며, permanent 영역에는 class, method 객체가 저장됩니다. WAS의 경우 수 많은 클래스들을 적재하게 되므로 permanent 영역을 많이 쓰게 되는데, 특히 어플리케이션 deploy/re-deploy 에 있어 OutOfMemoryError: PermGen space를 많이 겪게 됩니다. 
   - 이 때는 -XX:MaxPermSize 값을 올려줌으로써 해결 가능합니다.
   - 보통 기본값은 64m이므로 -XX:MaxPermSize=128m 또는 -XX:MaxPermSize=256m으로 설정합니다.
 
3. requested {size} bytes for {reason}. Out of swap space
   - JVM은 자바 힙 외에도 자체 구동을 위해 native heap을 사용합니다. 이 에러는 native heap이 부족할 때 발생합니다. 에러 메시지의 Out of swap space를 보고 단순히 swap을 늘리는 경우도 있는데, 메모리가 부족해 swap을 쓰게 될 경우 성능이 급격히 저하되므로 그 보다는 왜 swap이 필요하게 됐는지부터 찾아 원인을 제거하는 편이 바람직합니다.
 
   - 일단 JVM 힙 크기를 정할 때는 swap이 일어나지 않도록 가용한 물리적 메모리 안에서 결정해야 합니다. 이 때 조심할 점은 JVM 힙은 java 프로세스가 사용하는 메모리 중의 일부일 뿐이라는 겻을 염두에 둬야합니다.
 
   - java 프로세스 메모리 = heap 크기 + PermGen 크기 + (thread 개수 * thread stack 크기) + JVM C 메모리 로 계산 가능합니다.
 
   - 만약 가용 메모리가 1G가 남아있는데 -Xmx1g로 설정한다면 OutOfMemoryError는 피할 수 없게 될 것입니다. 
   - 따라서 다음과 같이 조치가 가능합니다.
     - java heap 크기를 줄인다. (-Xmx512m)
     - thread 수를 줄이거나 thread stack size를 줄인다. -Xss256k, -XXThreadStackSize=256k (Solaris)
     - JNI를 사용한다면 해당 모듈에 memory leak이 있는지 확인
 
4. unable to create new native thread
   - 이는 OS에서 어떤 이유로 쓰레드를 생성하는데 실패했을 때 발생합니다. 메모리가 부족해서일 수도 있고, OS에서 쓰레드 개수를 제한해서 일 수도 있습니다. top으로 확인했을 때 free 메모리가 많이 남아 있다면 OS에서 건 제한이 원인일 확률이 높습니다.
     - 메모리가 원인이라면 (3)번의 해결책을 동일하게 적용할 수 있습니다.
     - HP-UX라면 다음 커널 파러미터를 확인합니다.
          - max_thread_proc (# of threads per process)
          - nkthread (total number of threads)
          - ex) kmtune | grep max_thread_proc

결과적으로 스프링 파일 최대크기를 업로드 할 파일보다 여유롭게 설정했음에도 에러가 난 원인은 __파일을 업로드__ 하게 되면 해당 내용을 우선 __메모리에 담게 되고__ 다 담은 후 메모리에 있는 내용을 was에 전달한 뒤 HttpServletRequest 로 넘어오게 됩니다. 그런데 파일을 업로드 하면서 메모리에 파일이 써지다가 메모리 부족으로 Out Of Memory 에러가 발생하게 되버린 것이었습니다.

따라서 WAS가 파일 업로드를 처리하는것 보다는 static 파일을 처리할 수 있는 별도의 웹서버를 만드는게 수월할 것이라 판단됩니다. 상황에 따라 웹서버에서 파일을 업로드 한 뒤 비동기로 파일 업로드 완료여부에 따라 WAS에서 처리를 할 수도 있겠습니다.

