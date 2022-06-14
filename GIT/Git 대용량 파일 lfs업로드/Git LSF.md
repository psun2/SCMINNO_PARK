# `Git LSF(Large File Storage)`

git에 50MB 파일을 업로드시 Warning이 나타나고  
100MB파일을 업로드시 Error이 발생이 됩니다.

100MB이상의 파일을 업로드 하려고 Git LFS업로드를 사용 하고 있습니다.

---

## `다운로드`

[Git Large File Storage](https://git-lfs.github.com/)

---

## `Repository 적용 및 해제`

위 링크를 통해 LFS가 설치되어야 하는 선행 과정 후

```
git lfs install
```

및

```
git lfs uninstall
```

을 통하여 특정 repository에 lfs를 설치 합니다.

---

## `track`

lfs파일을 관리할때 파일을 track 합니다.  
해당 파일을 💥git에 add 하기전에, git lfs track을 해주어야 합니다.  
만약 기존에 add해둔 파일을 lfs로 관리를 해야하는 상황이라면

```
git rm --cache
```

로 먼저 unstaing을 시킨다음에 track을 해주어야 합니다.

```
git ifs track <file path>
git ifs track <file pattren>

ex)
git lfs track "*.mp4" // 확장자가 mp4인 파일들을 LFS로 관리
git lfs track "*.jar" // 확장자가 jar인 파일들을 LFS로 관리
git lfs track "image/test.png" // 특정파일을 LFS로 관리
```

track을 해주게 되면 .gitattributes파일이 생성 되는데,
.gitattributes파일을 먼저 add후 push하고 나머지 조각된 파일들을 push 할 수 있도록 해줍니다.
