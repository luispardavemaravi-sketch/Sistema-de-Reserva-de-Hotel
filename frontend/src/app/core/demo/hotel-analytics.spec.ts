import { describe, expect, it } from 'vitest';
import { LedgerEntry } from './hotel-admin.store';
import { chartBuckets, entriesForPeriod } from './hotel-analytics';

const row = (
  id: number,
  date: string,
  amount: number,
  kind: LedgerEntry['kind'],
  stayMode?: LedgerEntry['stayMode'],
): LedgerEntry => ({
  id,
  date,
  amount,
  kind,
  stayMode,
  description: `Movimiento ${id}`,
  method: 'Efectivo',
});

describe('financial periods', () => {
  const entries = [
    row(1, '2026-01-01', 35, 'Alojamiento', 'Horas'),
    row(2, '2026-02-08', 45, 'Alojamiento', 'Noche'),
    row(3, '2026-02-14', 7, 'Consumo'),
    row(4, '2026-03-11', -10, 'Gasto'),
    row(5, '2026-04-10', 40, 'Alojamiento', 'Horas'),
  ];

  it('includes every movement in a custom date range longer than one month', () => {
    const filtered = entriesForPeriod(entries, 'Rango personalizado', '2026-01-01', '2026-02-14');
    expect(filtered).toHaveLength(3);
    const buckets = chartBuckets(entries, 'Rango personalizado', '2026-01-01', '2026-02-14');
    expect(buckets.reduce((sum, bucket) => sum + bucket.lodging + bucket.consumption, 0)).toBe(87);
    expect(buckets.reduce((sum, bucket) => sum + bucket.hours, 0)).toBe(35);
  });

  it('groups long custom ranges by month without losing late entries', () => {
    const buckets = chartBuckets(entries, 'Rango personalizado', '2026-01-01', '2026-04-10');
    expect(buckets).toHaveLength(4);
    expect(buckets.reduce((sum, bucket) => sum + bucket.lodging, 0)).toBe(120);
    expect(buckets.reduce((sum, bucket) => sum + bucket.consumption, 0)).toBe(7);
  });
});
