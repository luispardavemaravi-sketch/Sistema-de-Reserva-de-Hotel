import { Injectable, signal } from '@angular/core';
import { ReportPeriod } from '../demo/hotel-analytics';

/** Presentation state only; source amounts remain in the existing demo ledger. */
@Injectable({ providedIn: 'root' })
export class AnalyticsViewService {
  readonly dashboardPeriod = signal<ReportPeriod>('Esta semana');
  readonly selectedType = signal<string | null>(null);
  readonly financePeriod = signal<ReportPeriod>('Esta semana');
  readonly financeFrom = signal('');
  readonly financeTo = signal('');
}
