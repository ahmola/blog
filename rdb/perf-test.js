import http from 'k6/http';
import { check, group } from 'k6';
import { Trend } from 'k6/metrics';
import { randomIntBetween } from 'k6';
import { SharedArray } from 'k6/data';

const postIds = new SharedArray('randomPostIds', function () {
  const ids = new Set();
  while (ids.size < 200) {
    ids.add(Math.floor(Math.random() * 10000) + 1);
  }
  return Array.from(ids);
});

export let options = {
  vus: 10,          // 동시 사용자 수
  iterations: 200,  // 총 요청 수 (RDB와 ES 각각 100씩)
};

let rdbTrend = new Trend('RDB_Response_Time');
let esTrend = new Trend('ES_Response_Time');

export default function () {
      for (let i = 0; i < postIds.length; i++) {
        const postId = postIds[i];

        group(`RDB 검색 테스트 - ID ${postId}`, function () {
          let rdbRes = http.get(`http://localhost:8081/api/v1/post?id=${postId}`);
          check(rdbRes, {
            'rdb status is 200': (r) => r.status === 200,
          });
          rdbTrend.add(rdbRes.timings.duration);
        });

        group(`ES 검색 테스트 - ID ${postId}`, function () {
          let esRes = http.get(`http://localhost:8080/api/v1/post?id=${postId}`);
          check(esRes, {
            'es status is 200': (r) => r.status === 200,
          });
          esTrend.add(esRes.timings.duration);
        });
      }
}