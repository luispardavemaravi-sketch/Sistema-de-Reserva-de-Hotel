import { RouterLink, ActivatedRoute } from '@angular/router';
import { AnalyticsViewService } from '../../core/services/analytics-view.service';
import { AnimatedNumberComponent } from '../../shared/motion/animated-number.component';
import { TiltDirective } from '../../shared/motion/tilt.directive';
import { BarChartComponent } from '../../shared/charts/bar-chart.component';
import { DonutChartComponent } from '../../shared/charts/donut-chart.component';
import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  HotelAdminStore,
  LedgerEntry,
  PaymentMethod,
  Promotion,
} from '../../core/demo/hotel-admin.store';
import { chartBuckets, entriesForPeriod, ReportPeriod } from '../../core/demo/hotel-analytics';
import { HotelDemoStore } from '../../core/demo/hotel-demo.store';
import { ModalFocusDirective } from '../../shared/components/modal-focus.directive';

type FinanceDialog = 'cash' | 'movement' | 'receipt' | 'tax' | 'promotion' | 'method' | null;

@Component({
  selector: 'app-finanzas',
  imports: [ReactiveFormsModule, ModalFocusDirective, RouterLink, AnimatedNumberComponent, TiltDirective, BarChartComponent, DonutChartComponent],
  templateUrl: './finanzas.component.html',
  styles: [
    `
      .period-bar {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        margin-bottom: 14px;
      }
      .period-bar .btn.active {
        background: #365fe0;
        color: #fff;
        border-color: #365fe0;
      }
      .range-fields {
        display: flex;
        gap: 8px;
        align-items: end;
        flex-wrap: wrap;
        margin-bottom: 18px;
      }
      .finance-metrics {
        display: grid;
        grid-template-columns: repeat(5, 1fr);
        gap: 10px;
        margin-bottom: 18px;
      }
      .finance-metrics .card {
        padding: 15px;
      }
      .finance-metrics strong {
        display: block;
        font-size: 22px;
        font-variant-numeric: tabular-nums;
        margin-top: 8px;
      }
      .finance-metrics small {
        color: var(--muted, #60717b);
      }
      .finance-grid {
        display: grid;
        grid-template-columns: 1.45fr 1fr;
        gap: 18px;
        margin-bottom: 18px;
      }
      .section-heading {
        display: flex;
        justify-content: space-between;
        align-items: start;
        gap: 12px;
      }
      .section-heading h2 {
        font-size: 16px;
        margin: 0;
      }
      .section-heading p {
        font-size: 12px;
        color: var(--muted, #5d6f79);
        margin: 5px 0;
      }
      .finance-bars {
        height: 230px;
        display: flex;
        gap: 8px;
        align-items: end;
        border-bottom: 1px solid var(--line, #e0e7ea);
        margin-top: 17px;
      }
      .finance-group {
        height: 100%;
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: end;
        align-items: center;
      }
      .finance-pair {
        height: 190px;
        width: 100%;
        max-width: 80px;
        display: flex;
        align-items: end;
        justify-content: center;
        gap: 3px;
      }
      .finance-pair i {
        display: block;
        width: 43%;
        min-height: 1px;
        border-radius: 3px 3px 0 0;
      }
      .finance-pair .lodging {
        background: #4d73f5;
      }
      .finance-pair .consumption {
        background: #39a7e5;
      }
      .finance-group span {
        font-size: 11px;
        color: var(--muted, #667681);
        margin: 8px 0 5px;
      }
      .chart-legend {
        display: flex;
        gap: 14px;
        color: var(--muted, #657580);
        font-size: 12px;
        margin-top: 15px;
      }
      .chart-legend i {
        display: inline-block;
        width: 9px;
        height: 9px;
        margin-right: 5px;
        border-radius: 2px;
      }
      .method-stack {
        display: flex;
        height: 23px;
        overflow: hidden;
        border-radius: 5px;
        margin: 20px 0;
      }
      .method-stack span {
        min-width: 0;
      }
      .method-row {
        display: flex;
        justify-content: space-between;
        gap: 10px;
        padding: 8px 0;
        border-bottom: 1px solid var(--line, #e5ebee);
        font-size: 13px;
      }
      .method-row i {
        display: inline-block;
        width: 9px;
        height: 9px;
        border-radius: 2px;
        margin-right: 7px;
      }
      .subgrid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 18px;
      }
      .action-row {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        margin: 14px 0;
      }
      .config-list {
        display: grid;
        gap: 8px;
        margin-top: 12px;
      }
      .config-list > div {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 10px;
        border-bottom: 1px solid var(--line, #e5ebee);
        padding: 8px 0;
        font-size: 13px;
      }
      .config-list button {
        font-size: 11px;
      }
      .error {
        color: #ad353b;
        font-size: 13px;
      }
      .summary-strip {
        display: flex;
        gap: 18px;
        flex-wrap: wrap;
        color: var(--muted, #5c6c76);
        font-size: 12px;
        margin-bottom: 18px;
      }
      .summary-strip strong {
        color: var(--ink, #202d35);
      }
      .movement-negative {
        color: #a23838;
      }
      .movement-positive {
        color: #327a49;
      }
      @media (max-width: 1100px) {
        .finance-metrics {
          grid-template-columns: repeat(3, 1fr);
        }
        .finance-grid,
        .subgrid {
          grid-template-columns: 1fr;
        }
      }
      @media (max-width: 650px) {
        .finance-metrics {
          grid-template-columns: repeat(2, 1fr);
        }
        .finance-metrics .card:last-child {
          grid-column: 1 / -1;
        }
        .finance-metrics strong {
          font-size: 18px;
        }
        .finance-bars {
          height: 190px;
        }
        .finance-pair {
          height: 150px;
        }
      }
    `,
  ],
})
export class FinanzasComponent {
  readonly store = inject(HotelDemoStore);
  readonly admin = inject(HotelAdminStore);
  private readonly fb = inject(FormBuilder);
  readonly periods: ReportPeriod[] = ['Hoy', 'Esta semana', 'Este mes', 'Rango personalizado'];
  readonly view = inject(AnalyticsViewService);
  private readonly route = inject(ActivatedRoute);
  readonly period = this.view.financePeriod;
  readonly rangeFrom = this.view.financeFrom;
  readonly rangeTo = this.view.financeTo;
  readonly fromDashboard = this.route.snapshot.queryParamMap.get('source') === 'dashboard';
  readonly incomeOnly = signal(this.route.snapshot.queryParamMap.get('incomeOnly') === 'true');
  readonly selectedMethod = signal<string | null>(null);
  readonly barData = computed(() => this.bars().map(row => ({ label: row.label, values: [row.lodging, row.consumption] })));
  readonly methodData = computed(() => this.methodTotals().map(row => ({ name: row.method, value: row.amount, color: row.color })));
  readonly movementDetails = computed(() => this.entries().filter(row =>
    (!this.incomeOnly() || row.amount > 0) && (!this.selectedMethod() || row.method === this.selectedMethod())));
  constructor() {
    const period = this.route.snapshot.queryParamMap.get('period');
    if (period && this.periods.includes(period as ReportPeriod)) this.period.set(period as ReportPeriod);
  }
  readonly dialog = signal<FinanceDialog>(null);
  readonly editingPromo = signal<number | null>(null);
  readonly error = signal('');
  readonly notice = signal('');
  readonly entries = computed(() =>
    entriesForPeriod(this.admin.ledger(), this.period(), this.rangeFrom(), this.rangeTo()),
  );
  readonly bars = computed(() =>
    chartBuckets(this.admin.ledger(), this.period(), this.rangeFrom(), this.rangeTo()),
  );
  readonly maxBar = computed(() =>
    Math.max(1, ...this.bars().map((row) => Math.max(row.lodging, row.consumption))),
  );
  readonly sales = computed(() =>
    this.entries()
      .filter((row) => row.amount > 0)
      .reduce((sum, row) => sum + row.amount, 0),
  );
  readonly expenses = computed(() =>
    Math.abs(
      this.entries()
        .filter((row) => row.kind === 'Gasto')
        .reduce((sum, row) => sum + row.amount, 0),
    ),
  );
  readonly lodging = computed(() =>
    this.entries()
      .filter((row) => row.kind === 'Alojamiento')
      .reduce((sum, row) => sum + row.amount, 0),
  );
  readonly consumption = computed(() =>
    this.entries()
      .filter((row) => row.kind === 'Consumo')
      .reduce((sum, row) => sum + row.amount, 0),
  );
  readonly averageTicket = computed(() => {
    const rows = this.entries().filter((row) => row.amount > 0);
    return rows.length ? Math.round(this.sales() / rows.length) : 0;
  });
  readonly todaySales = computed(() =>
    entriesForPeriod(this.admin.ledger(), 'Hoy')
      .filter((row) => row.amount > 0)
      .reduce((sum, row) => sum + row.amount, 0),
  );
  readonly weekSales = computed(() =>
    entriesForPeriod(this.admin.ledger(), 'Esta semana')
      .filter((row) => row.amount > 0)
      .reduce((sum, row) => sum + row.amount, 0),
  );
  readonly monthSales = computed(() =>
    entriesForPeriod(this.admin.ledger(), 'Este mes')
      .filter((row) => row.amount > 0)
      .reduce((sum, row) => sum + row.amount, 0),
  );
  readonly cashBalance = computed(
    () =>
      this.store.cashInitial() +
      this.todaySales() -
      Math.abs(
        entriesForPeriod(this.admin.ledger(), 'Hoy')
          .filter((row) => row.kind === 'Gasto')
          .reduce((sum, row) => sum + row.amount, 0),
      ),
  );
  readonly methodColors = ['#4d73f5', '#39a7e5', '#839bff', '#5b78bd', '#80c8ed', '#7289ac'];
  readonly methodTotals = computed(() =>
    this.admin.paymentMethods().map((method, index) => ({
      method,
      color: this.methodColors[index % this.methodColors.length],
      amount: this.entries()
        .filter((row) => row.amount > 0 && row.method === method)
        .reduce((sum, row) => sum + row.amount, 0),
    })),
  );
  readonly cashForm = this.fb.nonNullable.group({
    amount: [500, [Validators.required, Validators.min(0)]],
  });
  readonly movementForm = this.fb.nonNullable.group({
    kind: ['Ingreso'],
    description: ['', Validators.required],
    amount: [0, [Validators.required, Validators.min(0.01)]],
    method: ['Efectivo'],
  });
  readonly receiptForm = this.fb.nonNullable.group({
    type: ['Boleta'],
    guest: ['', Validators.required],
    amount: [0, [Validators.required, Validators.min(0.01)]],
    method: ['Efectivo'],
  });
  readonly taxForm = this.fb.nonNullable.group({
    igv: [18, [Validators.required, Validators.min(0), Validators.max(100)]],
  });
  readonly promotionForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    discount: [0, [Validators.required, Validators.min(0), Validators.max(100)]],
    active: [true],
  });
  readonly methodForm = this.fb.nonNullable.group({ name: ['', Validators.required] });
  setFrom(event: Event) {
    this.rangeFrom.set((event.target as HTMLInputElement).value);
  }
  setTo(event: Event) {
    this.rangeTo.set((event.target as HTMLInputElement).value);
  }
  barHeight(amount: number) {
    return (amount / this.maxBar()) * 100;
  }
  chartLabel() {
    return this.bars()
      .map((row) => `${row.label}: alojamiento ${row.lodging}, consumos ${row.consumption}`)
      .join('; ');
  }
  methodWidth(amount: number) {
    return this.sales() ? (amount / this.sales()) * 100 : 0;
  }
  open(kind: FinanceDialog, promo?: Promotion) {
    this.error.set('');
    this.dialog.set(kind);
    if (kind === 'cash') this.cashForm.setValue({ amount: this.store.cashInitial() });
    if (kind === 'tax') this.taxForm.setValue({ igv: this.admin.igv() });
    if (kind === 'promotion') {
      this.editingPromo.set(promo?.id || null);
      this.promotionForm.setValue({
        name: promo?.name || '',
        discount: promo?.discount || 0,
        active: promo?.active ?? true,
      });
    }
    if (kind === 'movement') {
      this.movementForm.reset();
      this.movementForm.patchValue({ method: this.admin.paymentMethods()[0] || 'Efectivo' });
    }
    if (kind === 'receipt') {
      this.receiptForm.reset();
      this.receiptForm.patchValue({ method: this.admin.paymentMethods()[0] || 'Efectivo' });
    }
  }
  close() {
    this.dialog.set(null);
    this.error.set('');
  }
  saveCash() {
    if (this.store.cashOpen()) {
      this.store.cashOpen.set(false);
    } else {
      if (this.cashForm.invalid) {
        this.error.set('Ingrese un fondo inicial válido.');
        return;
      }
      this.store.cashInitial.set(this.cashForm.getRawValue().amount);
      this.store.cashOpen.set(true);
    }
    this.close();
    this.notice.set('Cambio aplicado en demostración. No se guardó en PostgreSQL.');
  }
  saveMovement() {
    if (this.movementForm.invalid) {
      this.error.set('Complete descripción e importe.');
      return;
    }
    const v = this.movementForm.getRawValue();
    this.admin.addLedger({
      kind: v.kind as LedgerEntry['kind'],
      description: v.description.trim(),
      amount: v.kind === 'Gasto' ? -v.amount : v.amount,
      method: v.method,
    });
    this.close();
    this.notice.set('Cambio aplicado en demostración. No se guardó en PostgreSQL.');
  }
  saveReceipt() {
    if (this.receiptForm.invalid) {
      this.error.set('Seleccione un huésped e importe válido.');
      return;
    }
    if (!this.store.cashOpen()) {
      this.error.set('Abra la caja antes de emitir un comprobante.');
      return;
    }
    const v = this.receiptForm.getRawValue();
    const id = Math.max(0, ...this.store.receipts().map((row) => row.id)) + 1;
    this.store.receipts.update((rows) => [
      {
        id,
        type: v.type,
        number: `${v.type === 'Boleta' ? 'B' : 'F'}001-${String(id + 183).padStart(6, '0')}`,
        guest: v.guest,
        amount: v.amount,
        date: new Intl.DateTimeFormat('es-PE', { dateStyle: 'short', timeStyle: 'short' }).format(
          new Date(),
        ),
      },
      ...rows,
    ]);
    this.admin.addLedger({
      description: `${v.type} · ${v.guest}`,
      kind: 'Ingreso',
      amount: v.amount,
      method: v.method,
      sourceId: id,
    });
    this.close();
    this.notice.set('Cambio aplicado en demostración. No se guardó en PostgreSQL.');
  }
  saveTax() {
    if (this.taxForm.invalid) return;
    this.admin.igv.set(this.taxForm.getRawValue().igv);
    this.close();
    this.notice.set('Cambio aplicado en demostración. No se guardó en PostgreSQL.');
  }
  savePromotion() {
    if (this.promotionForm.invalid) {
      this.error.set('Ingrese nombre y descuento entre 0 y 100%.');
      return;
    }
    const value = this.promotionForm.getRawValue();
    const id = this.editingPromo();
    this.admin.promotions.update((rows) =>
      id === null
        ? [...rows, { id: Math.max(0, ...rows.map((row) => row.id)) + 1, ...value }]
        : rows.map((row) => (row.id === id ? { ...row, ...value } : row)),
    );
    this.close();
    this.notice.set('Cambio aplicado en demostración. No se guardó en PostgreSQL.');
  }
  deletePromotion(id: number) {
    this.admin.promotions.update((rows) => rows.filter((row) => row.id !== id));
  }
  saveMethod() {
    if (this.methodForm.invalid) return;
    const name = this.methodForm.getRawValue().name.trim();
    if (this.admin.paymentMethods().some((method) => method.toLowerCase() === name.toLowerCase())) {
      this.error.set('Ese método ya existe.');
      return;
    }
    this.admin.paymentMethods.update((rows) => [...rows, name]);
    this.methodForm.reset();
    this.close();
    this.notice.set('Cambio aplicado en demostración. No se guardó en PostgreSQL.');
  }
  deleteMethod(method: PaymentMethod) {
    this.admin.paymentMethods.update((rows) => rows.filter((row) => row !== method));
  }
}
