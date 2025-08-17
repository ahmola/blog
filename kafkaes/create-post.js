import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  iterations: 10000, // 1만개의 요청
  vus: 50,  // 가상 유저 수
};

export default function () {
  const url = 'http://localhost:8080/api/v1/post';
  const payload = JSON.stringify({
    title: `제목-${Math.random()}`,
    content: `내용-${Math.random()}`,
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post(url, payload, params);

  check(res, {
    'status is 200': (r) => r.status === 200,
  });
}