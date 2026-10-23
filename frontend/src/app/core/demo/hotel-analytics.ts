import { LedgerEntry } from './hotel-admin.store';

export type ReportPeriod = 'Hoy' | 'Esta semana' | 'Este mes' | 'Rango personalizado';
export interface ChartBucket {
  label: string;
  hours: number;
  night: number;
  lodging: number;
  consumption: number;
}

const key = (date: Date) =>
  `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
const dayStart = (value: string) => new Date(`${value}T12:00:00`);
export const todayKey = () => key(new Date());
export const dateShift = (date: Date, days: number) => {
  const copy = new Date(date);
  copy.setDate(copy.getDate() + days);
  return copy;
};
export function periodBounds(period: ReportPeriod, from?: string, to?: string) {
  const now = new Date();
  if (period === 'Rango personalizado' && from && to) return { start: from, end: to };
  if (period === 'Hoy') return { start: key(now), end: key(now) };
  if (period === 'Esta semana')
    return { start: key(dateShift(now, -((now.getDay() + 6) % 7))), end: key(now) };
  return { start: key(new Date(now.getFullYear(), now.getMonth(), 1)), end: key(now) };
}
export function entriesForPeriod(
  entries: LedgerEntry[],
  period: ReportPeriod,
  from?: string,
  to?: string,
) {
  const bounds = periodBounds(period, from, to);
  return entries.filter((entry) => entry.date >= bounds.start && entry.date <= bounds.end);
}
export function chartBuckets(
  entries: LedgerEntry[],
  period: ReportPeriod,
  from?: string,
  to?: string,
): ChartBucket[] {
  const bounds = periodBounds(period, from, to);
  if (period === 'Hoy') {
    return [0, 6, 12, 18].map((start) =>
      bucket(
        `${String(start).padStart(2, '0')}:00`,
        entries.filter(
          (entry) =>
            entry.date === bounds.start &&
            (entry.hour ?? 12) >= start &&
            (entry.hour ?? 12) < start + 6,
        ),
      ),
    );
  }
  if (period === 'Esta semana') {
    const monday = dayStart(bounds.start);
    return Array.from({ length: 7 }, (_, index) => {
      const date = key(dateShift(monday, index));
      return bucket(
        ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'][index],
        entries.filter((entry) => entry.date === date),
      );
    });
  }
  if (period === 'Este mes') {
    const month = new Date().getMonth();
    return Array.from({ length: 5 }, (_, index) =>
      bucket(
        `Sem ${index + 1}`,
        entries.filter((entry) => {
          const date = dayStart(entry.date);
          return date.getMonth() === month && Math.floor((date.getDate() - 1) / 7) === index;
        }),
      ),
    );
  }
  const start = dayStart(bounds.start);
  const end = dayStart(bounds.end);
  const days = Math.max(1, Math.floor((end.getTime() - start.getTime()) / 86_400_000) + 1);
  if (days > 90) {
    const months: ChartBucket[] = [];
    for (
      let date = new Date(start.getFullYear(), start.getMonth(), 1);
      date <= end;
      date.setMonth(date.getMonth() + 1)
    ) {
      const monthStart = key(date);
      const next = new Date(date.getFullYear(), date.getMonth() + 1, 1);
      const monthEnd = key(dateShift(next, -1));
      months.push(
        bucket(
          new Intl.DateTimeFormat('es-PE', { month: 'short', year: '2-digit' }).format(date),
          entries.filter(
            (entry) =>
              entry.date >= bounds.start &&
              entry.date <= bounds.end &&
              entry.date >= monthStart &&
              entry.date <= monthEnd,
          ),
        ),
      );
    }
    return months;
  }
  if (days > 31) {
    return Array.from({ length: Math.ceil(days / 7) }, (_, index) => {
      const weekStart = key(dateShift(start, index * 7));
      const weekEnd = key(dateShift(start, Math.min(days - 1, index * 7 + 6)));
      return bucket(
        `${weekStart.slice(5)}–${weekEnd.slice(5)}`,
        entries.filter((entry) => entry.date >= weekStart && entry.date <= weekEnd),
      );
    });
  }
  return Array.from({ length: days }, (_, index) => {
    const date = key(dateShift(start, index));
    return bucket(
      date.slice(5),
      entries.filter((entry) => entry.date === date),
    );
  });
}
function bucket(label: string, rows: LedgerEntry[]): ChartBucket {
  const amount = (kind: LedgerEntry['kind'], mode?: LedgerEntry['stayMode']) =>
    rows
      .filter((row) => row.kind === kind && (!mode || row.stayMode === mode))
      .reduce((sum, row) => sum + row.amount, 0);
  return {
    label,
    hours: amount('Alojamiento', 'Horas'),
    night: amount('Alojamiento', 'Noche'),
    lodging: amount('Alojamiento'),
    consumption: amount('Consumo'),
  };
}
