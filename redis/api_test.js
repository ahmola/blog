import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 10,          // 동시 가상유저 수
  duration: '1m',   // 총 실행 시간
};

export default function () {
  const url = 'http://127.0.0.1:8080/api/v1/post';
  const payload = JSON.stringify({
      title: `테스트 제목 ${Math.random()}`,
      content: `테스트 내용 ${Math.random()}`
  });

  const params = {
    headers: {
        'Content-Type': 'application/json',
    },
  };

  http.post(url, payload, params);
  sleep(0.1);       // RPS 조절
}