import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 10,          // 동시 가상유저 수
  duration: '1m',   // 총 실행 시간
};

export default function () {
    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const url = 'http://127.0.0.1:8080/api/v1/comment';
    const payload = JSON.stringify({
      userId: `테스트 유저 ${Math.floor(Math.random() * 10) + 1}`,
      content: `테스트 내용 ${Math.random()}`
    });

    http.post(url, payload, params);
    sleep(0.1);       // RPS 조절
}