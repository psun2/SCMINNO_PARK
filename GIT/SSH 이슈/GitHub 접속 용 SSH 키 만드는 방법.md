# `GitHub 접속 용 SSH 키 만드는 방법`

```
$ git push origin master
Logon failed, use ctrl+c to cancel basic credential prompt.
remote: Support for password authentication was removed on August 13, 2021. Please use a personal access token instead.
remote: Please see https://github.blog/2020-12-15-token-authentication-requirements-for-git-operations/ for more information.
fatal: Authentication failed for 'https://github.com/psun2/FastCampus-Lecture.git/'

$ git push 오리진 마스터
로그온에 실패했습니다. 기본 자격 증명 프롬프트를 취소하려면 ctrl+c를 사용하십시오.
원격: 2021년 8월 13일에 암호 인증 지원이 제거되었습니다. 대신 개인 액세스 토큰을 사용하십시오.
원격: 자세한 내용은 https://github.blog/2020-12-15-token-authentication-requirements-for-git-operations/를 참조하세요.
치명적: 'https://github.com/psun2/FastCampus-Lecture.git/'에 대한 인증 실패
```

위와 같은 오류로 인해 SSH KEY등록 이 필요한 상황입니다.

[공식문서의 SSH KEY](https://docs.github.com/en/github/authenticating-to-github/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)

[출처](https://www.lainyzine.com/ko/article/creating-ssh-key-for-github/#github%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%A0-%EB%95%8C-ssh-%ED%82%A4%EB%8A%94-%EC%99%9C-%ED%95%84%EC%9A%94%ED%95%9C%EA%B0%80%EC%9A%94)

GitHub를 사용하려면 SSH 키를 등록해야합니다.  
 Git과 GitHub를 처음 사용하면 SSH키를 왜 만들어야 하는지, 어떻게 만들어야하는지, 그리고 따라서 키를 만들어봤는데 잘 만들어진 건지 확인하는 데 어려움을 느낄 수 있습니다.  
 아래 내용을 따라온다면 어렵지 않게 SSH 키를 만들고, GitHub를 사용해볼 수 있습니다.

---

## `GitHub를 사용할 때 SSH 키는 왜 필요한가요?`

로컬 개발 환경에서 Git을 단독으로 사용한다면 SSH가 없어도 무방합니다만, 안전하게 외부 Git 서버에서 코드를 Clone하거나 Push하려면 SSH 프로토콜을 사용합니다.  
GitHub처럼 인터넷을 통해 원격에서 Git 저장소를 호스팅해주는 서비스에서도 SSH 프로토콜을 지원하고 있습니다.  
GitHub 뿐만 아니라 원격 Git 저장소와 통신할 때 일반적으로 사용하는 방법입니다.

GitHub를 사용하고 싶은데 SSH 이야기가 나오니 잘 이해가 되지 않을 수도 있습니다.  
SSH는 Git에서도 사용하지만, 원래는 멀리 떨어져있지만 인터넷이나 네트워크를 통해연결되어있는 컴퓨터에 안전하게 연결해주는 프로토콜로 안전한 셸(Secure Shell)이라고도 부릅니다.  
윈도(Windows)의 명령 프롬프트나 리눅스의 터미널을 사용해봤다면 셸은 이미 알고 있는 겁니다.  
셸을 통해서 명령어를 입력할 수 있는데, 이 명령어를 내 컴퓨터가 아닌 네트워크로 다른 컴퓨터에 전달해주고 결과를 받아볼 수 있게 해주는 게 바로 SSH입니다.

여기서 또 하나 중요한 개념이 바로 안전(Secure)한 방식으로 통신한다는 점입니다. SSH는 사용자, 패스워드나 여러가지 인증 방법을 지원합니다만, 그 중에서도 편리성이나 안정성 면에서 추천하는 방식이 공개키 인증 방식입니다.

<b>공개키 인증 방식을 사용하려면 공개키와 개인키를 한 쌍 만들어야합니다.</b>  
아주 간단하게 설명하면, 공개키는 접속하고자 하는 서버에 등록해놓는 용도로 사용합니다. 이름에서 알 수 있지만 공개키는 외부에 공개되어도 괜찮습니다.  
사용자는 개인키를 통해서 SSH에 접속하고, 연결 요청을 받은 SSH 서버에서는 서버에 등록된 공개키 중에 요청 받은 개인키 정보와 매치가 되는 공개키가 있는지 찾습니다.  
없으면 서버 접속(인증)에 실패합니다. 등록된 공개키가 있다면? 🎩 짠, 인증에 성공하고 서버에 접속이 됩니다.

공개키는 어디에 공개되어도 문제가 없습니다만, <b>개인키는 비밀번호와 마찬가지로 반드시 나만 접근할 수 있도록 안전하게 보관해야합니다.</b>  
절대로 다른 사람에게 노출되어서는 안 됩니다.  
개인키가 있으면 내가 공개키를 등록해놓은 SSH 서버나 Git 서버에 접속할 수 있기 때문입니다.

---

## `SSH 공개키와 개인키 만들기`

그럼 SSH를 왜 사용하는지 알아봤으니, SSH 키를 직접 만들어보고 GitHub에 등록해보겠습니다.

윈도우에서는 Git을 설치하면 함께 설치되는 Git Bash를 사용합니다. macOS나 Linux에서는 터미널을 실행해서 진행합니다.

SSH 키를 만들기 전에 이미 키가 만들어져 있는지 확인해보는 것을 추천합니다.  
무턱대고 키를 추가 발급하면 기존 키가 덮어씌워질 수 있습니다.

```
$ cd ~/.ssh
$ ls
```

먼저 ~/.ssh 디렉터리로 이동해서 ls를 실행해 id_ed25519와 id_ed25519.pub 혹은 id_rsa와 id_rsa.pub 파일쌍이 있는지 확인해봅니다.  
이 파일이 있다면 이미 키를 생성했던 적이 있는 것입니다.  
다른 이름으로 여러개의 키를 만들어서 사용하는 것도 물론 가능하지만, 개인키의 위치를 따로 지정해줘야해서 불편합니다.

이 파일들이 없다면 ssh-keygen으로 생성합니다. 생성 방법은 간단합니다. 아래 명령어를 실행해줍니다.

```
$ ssh-keygen -t ed25519 -C "your_email@example.com"
```

-C 옵션의 your_email@example.com은 자신의 이메일로 변경해주세요.  
ed25519 방식으로 동작하지 않는 경우에는 아래와 같이 RSA로 옵션을 변경해 SSH 키를 생성합니다.

```
$ ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
or
$ ssh-keygen -t rsa -C "your_email@example.com"
```

명령어를 실행해서 실제로 키를 만들어보겠습니다.

```
Generating public/private ed25519 key pair.
Enter file in which to save the key (/Users/lainyzine/.ssh/id_ed25519):
```

첫 번째로 저장하고자하는 위치를 물어봅니다. 다른 위치를 지정하는 것도 가능하지만 여기서는 기본값을 사용합니다.

```
Enter passphrase (empty for no passphrase):
Enter same passphrase again:
```

다음으로 SSH 키에 대한 비밀번호를 추가로 지정할지 물어봅니다.  
패스워드를 설정해도 되고, 추가 패스워드 없이 사용하려면 엔터를 두 번 입력해줍니다. (추가 패스워드는 나중에 지정하는 것도 가능하지만, GitHub에서는 공식적으로 패스워드 설정을 권장하고 있습니다).

```
Your identification has been saved in /c/Users/parksungun/.ssh/id_rsa
Your public key has been saved in /c/Users/parksungun/.ssh/id_rsa.pub
The key fingerprint is:
SHA256:FqA6kqIOquVqN8WHHLhcT6lBzc9/J6QNKodbugGSZJM your_email@example.com
The key's randomart image is:
+---[RSA 4096]----+
|      +          |
|    .o +         |
|   E+   =        |
| .ooo+ o + . .   |
|+ +o=.B S o =    |
|o. +.*.* + o + . |
|o . . ..*   . o  |
|++ o   o.        |
|*oo .  ..        |
+----[SHA256]-----+
```

그럼 SSH 키가 생성되었습니다.  
설명을 잘 읽어보면  
개인키는 /c/Users/parksungun/.ssh/id_rsa에,  
공개키는 /c/Users/parksungun/.ssh/id_rsa.pub에 저장되었습니다.  
RSA 방식으로 키를 생성하는 경우 기본 파일 이름은 id_rsa, id_rsa.pub가 됩니다.

실제로 키가 잘 생성되었는지 개인키를 출력해봅니다.

```
$ cd ~/.ssh


$ cat id_ed25519

-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAACmFlczI1Ni1jdHIAAAAGYmNyeXB0AAAAGAAAABAfS46sz/
W6su8L3N3CJN2TAAAAEAAAAAEAAAAzAAAAC3NzaC1lZDI1NTE5AAAAIAd599uuWmQMawaN
IXTMl+LF4+7Jph1DLEYHPk+lF90bAAAAoHn+SkTjQzEaE4pjKztoTdRObobfWmHJkWhMFU
x0gjimvIdcE/YUbIQah9GFxmntbNGDkIJYzQxxV+21Xb9MZ3bZAILG10ktPCYbmEoJ47Hk
M3wYMWlHiYvTsCJlwe8llzro//TE+kF5liZN7nSBM/jPFDSoGMSRknD+TffY4amV43U49c
jTCgrTvQdDmBoIHh1fEhR1TAjdQ3bctfZhinM=
-----END OPENSSH PRIVATE KEY-----
```

다시 한 번 강조하지만, 개인키는 절대 공개되어서는 안 됩니다.

이번에는 공개키를 출력해봅니다.  
바로 이 내용을 GitHub에 등록해주어야합니다.

```
$ cd ~/.ssh

$ cat id_ed25519.pub

ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIAd599uuWmQMawaNIXTMl+LF4+7Jph1DLEYHPk+lF90b tjddjs90@gmail.com
```

이 내용을 전체를 직접 복사하거나, macOS에서는 pbcopy, 윈도에서는 clip 명령어를 사용해 클립보드에 복사합니다.  
cat으로 실행한 내용을 직접 복사하기보다는 텍스트가 깨지지 않도록 명령어를 통해 복사하는 걸 추천합니다.

```
# macOS
$ pbcopy < ~/.ssh/id_ed25519.pub

# Windows
$ clip < ~/.ssh/id_ed25519.pub
```

---

## `공개키를 GitHub 계정에 등록하기`

GitHub을 사용하려면 먼저 GitHub의 계정이 필요합니다.  
아직 없다면 먼저 GitHub에 가입합니다.

[GitHub](https://github.com/)

![SSH_KEY 등록1](./images/등록1.png)  
![SSH_KEY 등록2](./images/등록2.png)  
로그인 후 오른쪽 상단의 프로필을 클릭하고, Settings 메뉴로 이동합니다.  
오른쪽 사이드 바에서 SSH and GPG keys 메뉴를 선택합니다.  
오른쪽 상단의 New SSH key를 클릭하면 SSH 키 등록창이 나타납니다.  
아래 링크로 바로 이동할 수도 있습니다.

![SSH_KEY 등록3](./images/등록3.png)  
Title 필드에는 등록하려는 키의 이름을 입력합니다. 이메일이나 본인이 구분하기 편리하도록 이름을 정해줍니다.

Key 필드에는 앞에서 복사한 공개키의 그대로 입력합니다. 줄바꿈이나 다른 문자가 들어가면 제대로 동작하지 않을 수 있으니 주의가 필요합니다.

Add SSH key를 눌러 키를 등록합니다.

![SSH_KEY 등록4](./images/등록4.png)  
SSH 키를 생성하고 GitHub에 등록하는 과정까지 마쳤습니다.

공개키는 정말로 공개키입니다.  
GitHub에 키를 등록하면 github.com/[USERNAME].keys으로 접속하면 해당 사용자의 공개키를 확인할 수 있습니다.

![SSH_KEY 확인](./images/확인.png)  
공개키를 알아서 할 수 있는 건, 대응하는 개인키를 가진 사용자에게 인증 권한을 부여하는 일입니다. 따라서 공개키가 공개된다고 보안 상 위험은 없다고 봐도 무방합니다.

그럼 이제 등록한 키가 잘 동작하는지 확인해볼 차례입니다.

---

## `SSH 접속 설정`

테스트에 앞서 명시적으로 SSH 접속 설정을 해두겠습니다. ~/.ssh/config 파일에 아래 내용을 추가하거나, 파일이 없다면 생성한 후 아래 내용을 추가해주세요.

```
Host github.com
  IdentityFile ~/.ssh/id_ed25519
  User git
```

이제 접속 테스트를 해볼 차례입니다. 아래 명령어로 GitHub에 접속 테스트를 해볼 수 있습니다.

```
$ ssh -T git@github.com
Enter passphrase for key '/Users/lainyzine/.ssh/id_ed25519':
Hi lainyzine! You've successfully authenticated, but GitHub does not provide shell access.
```

SSH 키에 패스워드를 지정한 경우 패스워드 입력을 받습니다.  
인증에 성공하면 GitHub ID 출력까지 확인할 수 있습니다.  
이를 통해 등록한 SSH 키로 접속에 성공한 것을 확인할 수 있습니다. (여기서 에러가 발생한다면 트러블슈팅 섹션을 참고해주세요)

하나의 GitHub 계정을 사용할 때는 큰 문제가 없습니다만, 멀티 어카운트를 사용하는 경우에는 설정이 좀 더 복잡해집니다. 이에 대한 내용은 아래 글에서 다룹니다.

---

## `팁: SSH키 패스워드 입력을 생략하는 방법`

매번 SSH 키 패스워드 입력을 하려면 번거롭습니다. ssh-agent에 키를 등록해두면 좀 더 편리하게 사용할 수 있습니다.  
ssh-agnet는 백그라운드에서 SSH 인증 정보를 관리합니다. ssh-agent가 실행되어있는지부터 확인해봅니다.

```
$ ssh-add -l
Error connecting to agent: Connection refused
```

위와 같이 에러가 나오면 ssh-agent가 실행되어있지 않은 상태입니다.  
SSH 키 목록이나 The agent has no identities.와 같은 메시지가 나오면 이미 ssh-agent가 실행된 상태입니다.  
실행되어있지 않다면, 먼저 ssh-agent를 실행합니다.

```
$ eval "$(ssh-agent -s)"
```

다음으로 ssh-add 명령어로 키를 등록합니다. 등록할 대 패스워드를 확인합니다.

```
$ ssh-add ~/.ssh/id_ed25519
Enter passphrase for /Users/lainyzine/.ssh/id_ed25519:
Identity added: /Users/lainyzine/.ssh/id_ed25519 (your_email@example.com)
```

ssh-add -l 명령어로 ssh-agent에 등록된 SSH 키들을 확인할 수 있습니다.

```
$ ssh-add -l
256 SHA256:JfXX05Pj352343pnuQiQ125Z6V88Q/F49cBViSumqVFeSp3LIQs lainyzine.com@gmail.com (ED25519)
```

이 상태에서 git clone을 하면 더 이상 패스워드를 물어보지 않습니다.

---

## `팁: macOS에서 키체인 사용하기`

macOS에서는 ssh-add 명령어를 사용할 때 -K 옵션을 붙여주면 패스워드를 키체인에 저장해서 사용할 수 있습니다.

```
$ ssh-add -K ~/.ssh/id_ed25519
Enter passphrase for /Users/lainyzine/.ssh/id_ed25519:
Identity added: /Users/lainyzine/.ssh/id_ed25519 (lainyzine.com@gmail.com)
```

처음 패스워드를 입력하면 키체인에 패스워드를 등록하고 패스워드를 다시 물어보지 않습니다.

~/.ssh/config 파일에 AddKeysToAgent와 UseKeychain 옵션을 추가해두면 위의 과정들을 자동적으로 처리해줘서 편리합니다.

```
Host github.com
  IdentityFile ~/.ssh/id_ed25519
  User git
  AddKeysToAgent yes
  UseKeychain yes
```

---

## `트러블 슈팅: Permission denied`

ssh -T git@github.com로 SSH 접속 테스트를 할 때 에러가 발생할 수 있습니다. Permission denied 가 발생하는 경우 에러메시지를 잘 확인해보면 도움이 됩니다. 예를 들어 다음과 같이 출력되는 경우에는,

```
$ ssh -T git@github.com
no such identity: /Users/lainyzine/.ssh/id_ed25519: No such file or directory
git@github.com: Permission denied (publickey).
```

No such file or directory 메시지에서 힌트를 얻을 수 있습니다. 지정된 키 파일이 존재하지 않는 경우입니다. 키가 제대로 생성되었는지, 키 경로에 오타는 없는지 확인해봅니다. -i 옵션을 사용해 SSH 키 경로를 명시적으로 지정해보는 것도 좋습니다.

```
$ ssh -i /Users/lainyzine/.ssh/id_ed25519 -T git@github.com
```

접속 과정을 좀 더 자세히 확인하고 싶다면 -v 옵션을 붙여봅니다. 상세한 로그가 나와서 문제를 찾는데 도움이 됩니다.

```
$ ssh -v -T git@github.com
```
