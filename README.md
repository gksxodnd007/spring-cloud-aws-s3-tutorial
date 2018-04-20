# AWS S3 튜토리얼

## 개발환경
- **JDK 1.8**
- **Spring boot**
- **JPA / Hibernate**
- **MySQL**
- **Gradle**

## File Storage란?

-	Object storage보다 오래된 storage로써 사람들에게 더 잘 알려져있음.
-	파일,데이터의 이름을 지정하고 폴더에 저장 한 다음 더 많은 폴더 아래에 파일을 중첩시켜 설정된 경로를 만들 수 있음.
-	파일들은 계층적으로 구조화 됨(directory 밑에 directory -> 리눅스 파일시스템을 생각하면 됨)
-	파일에는 제한적인 메타정보가 저장됨.

> 파일명, 생성날짜, 최근 수정된 날짜 등등

### Advantage

-	작은 파일에 대해서는 최고의 성능을 냄

### Disadvantage

-	어느 임계점 까지는 잘 동작하지만 한계치에 도달하면 성능이 저하
-	NAS 시스템 자체는 처리 능력이 제한되어있어 프로세서에 병목 현상이 발생


## Object Storage란?

-	비정형 데이터를 안정하게 저장하고 관리하기 위해 등장
-	오브젝트 스토리지는 파일에 대한 구체적인 정보를 포함한 메타데이터를 자동으로 생성, 보관, 관리 하는 방식으로 데이터를 저장

> 메타데이터는 커스터마이징 할 수 있다. 사용자가 부가적인 메타데이터 정보를 추가하여 저장 가능

-	데이터의 물리적인 위치는 상관 없이 사용자나 서버에서 식별할 수 있는 ID를 오브젝트에 부여하여 관리하는 스토리지
- 파일이라는 객체를 컨테이너안에 저장하여 관리
-	데이터와 식별 가능한 ID, 메타데이터를 하나의 오브젝트로 묶고 이것을 컨테이너에 저장하고 동일한 레벨로 관리 하는 방식을 취함
-	이런 관리 방법을 통해 파일을 저장할 때, 해당 파일과 메타데이터를 생성하여 함께 관리 하는 방식을 가짐
-	Flat address로 저장됨

> Flat address란 메모리의 주소지정 방식을 생각하면된다. 32bit computer라면 0 ~ 4GB까지 순차적으로 메모리의 주소값을 리소스에 할당한다.

### Advantage

-	비정형 데이터에 정형성을 부여함으로서 훨씬 많은 양의 콘텐츠를 보다 효율적이고 지능적인 방식으로 저장 관리 할 수 있도록 함
-	비정형 데이터라도 메타데이터를 이용해 신속한 파일 검색 및 분석으로 또 다른 가치를 얻거나 링크 주소를 통한 파일 공유 등 비정형 데이터에 대한 접근 지원
-	Flat address space에 저장되기 때문에 여러 지역에서 데이터를 쉽게 찾고 검색 가능
-	This flat address space also helps with scalability. By simply adding in additional nodes, you can scale to petabytes and beyond.

### Disadvantage

-	Throughput 문제가 있음

> 하나의 파일을 업데이트 할 때마다 모든 복제본이 업데이트 될 때까지 기다려야 하기 때문에 데이터를 자주 바꿔야 하는 업무에는 권장하지 않음

## OBJECT STORAGE VS FILE STORAGE

|             | OBJECT STORAGE                                       | FILE STORAGE                                                      |
|:------------|:-----------------------------------------------------|:------------------------------------------------------------------|
| PERFORMANCE | best for big content and high stream throughput      | best for smaller files                                            |
| GEOGRAPHY   | Data can be stored across multiple regions           | Data typically needs to be shared locally                         |
| SCALABILITY | Scales infinitely to petabytes and beyond            | Potentially scales up to millions of files, but can’t handle more |
| ANALYTICS   | Customizable metadata, not limited to number of tags | Limited number of set metadata tags                               |

## AWS S3

### AWS S3의 장점

Amazon S3는 의도적으로 단순성 및 견고성에 초점을 두는 최소한의 기능 세트를 사용하여 구축됨. 다음은 Amazon S3의 일부 장점들이다.

- **버킷 만들기**
  - 데이터를 저장하는 버킷을 만들고 여기에 이름을 지정.
  - 버킷은 데이터를 저장하기 위한 Amazon S3의 기본 컨테이너.

- **버킷에 데이터 저장**
  - 버킷에 데이터를 무한정으로 저장. Amazon S3 버킷에 객체를 원하는 만큼 업로드할 수 있으며, 각 객체에 최대 5TB의 데이터를 포함할 수 있음.
  - 각 객체는 고유한 개발자 할당 키를 사용하여 저장 및 검색

- **데이터 다운로드**
  - 언제든지 데이터를 직접 다운로드하거나 다른 사람이 그렇게 하도록 허용가능

- **권한**
  - 데이터를 Amazon S3 버킷으로 업로드 또는 다운로드하려는 사용자에게 액세스 권한을 부여하거나 해당 권한을 거부 가능.
  - 3가지 유형의 사용자에게 업로드 및 다운로드 권한을 부여할 수 있음. 인증 메커니즘을 사용하면 데이터가 무단으로 액세스되지 않도록 보호하는 데 도움이 될 수 있음.

- **표준 인터페이스**
  - 모든 인터넷 개발 도구 키트에서 사용할 수 있도록 설계된 표준 기반 REST 및 SOAP 인터페이스를 사용.

> **참고**
  HTTP를 통한 SOAP 지원은 중단되었지만 HTTPS를 통해 계속해서 사용할 수 있습니다. 새로운 Amazon S3 기능은 SOAP에 대해 지원되지 않습니다. REST API 또는 AWS SDK를 사용하는 것이 좋습니다.

**AWS S3에 대한 개념및 용어 정리**
https://docs.aws.amazon.com/ko_kr/AmazonS3/latest/dev/Introduction.html#BasicsBucket

### AWS S3 이용방법

![main](https://i.imgur.com/WifcxnN.png){: width="100" height="100"}

![BucketBtn](https://i.imgur.com/rdNNsZO.png)

![1](https://i.imgur.com/Yqj1zIz.png)

![2](https://i.imgur.com/27FQNte.png)

![3](https://i.imgur.com/Q7OaWs2.png)

![4](https://i.imgur.com/r9lpexQ.png)

![security_1](https://i.imgur.com/qiP0KOM.png)

![security_2](https://i.imgur.com/iabkI7S.png)

![security_3](https://i.imgur.com/LPaWg8z.png)

![security_4](https://i.imgur.com/MlBdFqJ.png)

![security_5](https://i.imgur.com/abLabGC.png)

![security_6](https://i.imgur.com/rzU5mkm.png)

![security_7](https://i.imgur.com/9u5Bibn.png)

![security_8](https://i.imgur.com/rhObH0L.png)
