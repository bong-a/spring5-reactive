

#  왜 리액티브 스프링인가?

## 왜 리액티브인가?

필수 시스템 설계원리로서 반응성의 가치를 이해해보자.

소규모 비즈니스를 개발하고 있다 가정해보자. 

- 웹기반 온라인 상점
- 전통적인 개발 방법 선택
- 시간당 약 1000명 방문
- 구성
  - 톰캣 웹 서버, 500개 스레드 풀 , 평균 응답시간 250밀리초
  - 단순계산시 초당 약 2000명(=500x1/0.25=500x4)
  - 현재 시스템용량은 평균 부하 처리하기에 충분함
- 11월 마지막 블랙프라이데이가 올 경우
  - 폭발적인 고객 증가 -> 스레드 풀에 사용자 요청을 처리할 스레드 부족 -> 응답시간 지연-> 서비스 다운 -> 고객이탈 -> 경제적 손실 증가
  - 실제 사례 : 아마존닷컴 정전사건,월마트 블랙프라이데이 재난 등



**어떻게 대응할 것인가?**가 중요한 문제이다. 

애플리케이션은 변화에 대응해햐한다. -> 수요의 변화 및 외부 서비스의 가용성 변화에 대응할 수 있어야 한다.

그래서 **어떻게 대응할 것인가?**

- **탄력성(elasticity)** : 다양한 작업부하에서 응답성을 유지하는 능력

  더 많은 사용자가 작업을 시작할 때 시스템 처리량이 자동으로 증가해야한다.

  애플리케이션 관점에서  탄력성을 사용하면 평균지연시간에 영향을 미치지 않고 시스템을 확장할수있기 때문에 응답성을 유지할 수 있다.

  - 예시 : 추가 인스턴스를 제공해 시스템 처리량을 증가시킨다. -> 응답성 향상

    ​		반면, 수요가 낮은 경우 리소스 사용측면에서 시스템을 축소해 비즈니스 비용 절감한다.

    -> 수평적 또는 수직적 확장을 통해 탄력성을 달성할 수 있다.



분산시스템에서의 확장성을 달성하기 위해 시스템 병목지점 또는 동기화 지점을 확장하는 정도에 그친다.

- [암달의 법칙](https://bong-a.github.io/gitbook/etc/Amdahls-law.html) & 건터의 보편적 확장성 모델

응답성을 유지하는 능력을 갖추지 않고 확장 가능한 분산 시스템을 구축하는 것은 어려운 일이다.

- 외부결제서비스가 중단됨 -> 상품구매결제가 실패하는 상황 -> 응답성을 떨어트리는 상황 -> 고객이탈

그래서 **어떻게 대응할 것인가?**

- **복원력** : 시스템 실패에도 반응성을 유지할 수 있는 능력

  시스템에 대한 허용기준은 시스템 복원력을 유지하는 것

  - 예시 1 : 아마존 결제서비스 중단되도 주문 먼저 접수하고 이후에 자동으로 결제 재시도
  - 예시 2: 댓글서비스장애에도 장바구니 확인이나 결제서비스는 아무런 영향이 없이 동작

#### 탄력성과 복원력은 밀접하게 결합되어 있다. 이 두가지를 모두 사용할 때만 시스템의 진정한 응답성을 달성 할 수 있다.



### 메세지 기반 통신

분산 시스템에서 컴포넌트를 연결하는 방법과 낮은 결합도, 시스템 격리 및 확장성 유지를 어떻게 하면 동시에 달성할 수 있는지는 여전히 불분명하다.

- HTTP 이용해 컴포넌트간의 통신을 수행하는 상황을 검토

  - 사용자 호출할 때마다 외부 서비스에 대한 추가 HTTP 요청을 발생시키고 다음 차례가 실행
  - 문제점 : 처리 시간의 일부만 효과적인 CPU사용을 위해 할당되고, 나머지 시간동안 메인 스레드는 I/O 차단되며 다른 요청을 처리할 수 없다.
  - 자바 병령처리 위해 스레드 풀이 있지만, 부하가 높은 상태에서 이러한 기법이 새로운 I/O작업을 동시에  처리는데 매우 비효율적일 수 있다. -> 자세한 내용은 6장에서...

  - I/O 측면에서 리소스 활용도 높이려면 비동기 논블로킹모델 사용해야함 
    - 대표적인 예시 : 문자 메세지

분산시스템에서 서비스간에 통신할 때 자원을 효율적으로 사용하기 위해서는 **메세지 기반(message-driven) 통신 원칙**을 따라야한다.

- 메세지브로커를 사용하면 메세지 대기열을 모니터링해 시스템이 부하 관리 및 탄력성을 제어할 수 있다.

메세지통신은

- 명확한 흐름 제어를 제공
- 전체 설계를 단순화             -> 자세한 내용은 8장에서...



다시 정리하자면 

## 리액티브 시스템의 기본원리

- 응답성 -> 분산시스템으로 구현되는 모든 비즈니스의 핵심 가치
- 탄력성
- 복원력
- 메세지기반

높은 응답성 확보 -> 탄력성 및 복원력이 이미 이루어져있다. 

탄력성 및 복원력 확보하는 방법 :메세지 기반 통신 사용

이러한 원칙에 따라 구축된 시스템은 모든 구성 요소가 독립적이고 적절히 격리돼있어 유지 보수 및 확장이 용이하다.

## 

### 반응성에 대한 유스케이스

리액티브 시스템의 적용 영역은 다양한 영역에 적용될 수 있다.

- MSA 기반의 웹스토어
  - 서비스 복제본 구성을 통해 응답성 확보
  - 장애 복원력을 위해 카프카 구성
- 애널리틱스 분야 ( 스트리밍 아키텍처)
  - 빅데이터 처리 및 실시간 통계 제공
  - 짧은 지연시간과 높은 처리량이 특징
  - 응답하거나 전달하는 능력이 매우 중요 -> 가용성이 높은 시스템 구축해야함 -> 리액티브 기본원리에 의존해야함
  - 배압을 활성화하여 복원성 확보



## 왜 리액티브 스프링인가?

위의 설명들은 아키텍처관점에서의 설명들이였다.

구현 관점에서 리액티브 시스템을 쉽게 구축하려면 프레임워크를 잘 선택해야한다.

잘 알려진 프레임워크 Akka, Vert.x

스프링 프레임워크는 개발자에게 친숙한 프로그래밍 모델을 사용해 웹 애플리케이션을 구축할수 있는 방법 제공

하지만 리액티브 시스템 구축하는데 제약사항이 있음 

그러나 스프링 클라우드 프로젝트 덕분에 리액티브 시스템구축하는데 적합할 수 있다. 

