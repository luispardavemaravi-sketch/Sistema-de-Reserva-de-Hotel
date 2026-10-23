import { writeFileSync } from 'node:fs';

const url = process.env.PUBLIC_API_URL || 'http://localhost:8080/api';
if (!/^https?:\/\/[^\s]+\/api\/?$/.test(url)) {
  throw new Error('PUBLIC_API_URL debe terminar en /api');
}
writeFileSync(new URL('../src/environments/environment.ts', import.meta.url),
  `export const environment = { apiUrl: ${JSON.stringify(url.replace(/\/$/, ''))} } as const;\n`);
