import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { FinanzasComponent } from '../finanzas/finanzas.component';
import { HotelAdminStore } from '../../core/demo/hotel-admin.store';
import { AuthService } from '../../core/services/auth/auth.service';
import { todayKey } from '../../core/demo/hotel-analytics';

describe('Dashboard and finance source consistency', () => {
  it('shows the same today total, updates from the source and retains dashboard filters', () => {
    TestBed.configureTestingModule({ providers: [
      { provide: AuthService, useValue: { canAccess: () => true } },
      { provide: ActivatedRoute, useValue: { snapshot: { queryParamMap: convertToParamMap({ period: 'Hoy', source: 'dashboard', incomeOnly: 'true' }) } } },
    ] });
    const store = TestBed.inject(HotelAdminStore);
    store.ledger.set([
      { id: 1, date: todayKey(), amount: 42, kind: 'Alojamiento', method: 'Efectivo', description: 'Fixture' },
      { id: 2, date: todayKey(), amount: -10, kind: 'Gasto', method: 'Efectivo', description: 'Expense fixture' },
    ]);
    const dashboard = TestBed.runInInjectionContext(() => new DashboardComponent());
    dashboard.selectedPeriod.set('Este mes');
    const finance = TestBed.runInInjectionContext(() => new FinanzasComponent());
    expect(dashboard.todayRevenue()).toBe(42);
    expect(finance.sales()).toBe(42);
    expect(finance.movementDetails().length).toBe(1);
    store.ledger.update(rows => [...rows, { id: 3, date: todayKey(), amount: 8, kind: 'Consumo', method: 'Yape', description: 'Fixture' }]);
    expect(dashboard.todayRevenue()).toBe(50);
    expect(finance.sales()).toBe(50);
    expect(finance.movementDetails().reduce((sum, row) => sum + row.amount, 0)).toBe(50);
    const returning = TestBed.runInInjectionContext(() => new DashboardComponent());
    expect(returning.selectedPeriod()).toBe('Este mes');
  });
});
