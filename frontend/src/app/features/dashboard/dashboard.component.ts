import { AnimatedNumberComponent } from '../../shared/motion/animated-number.component';
import { TiltDirective } from '../../shared/motion/tilt.directive';
import { TooltipDirective } from '../../shared/motion/tooltip.directive';
import { DonutChartComponent } from '../../shared/charts/donut-chart.component';
import { BarChartComponent } from '../../shared/charts/bar-chart.component';
import { AnalyticsViewService } from '../../core/services/analytics-view.service';
import { AuthService } from '../../core/services/auth/auth.service';
import { isPlatformBrowser } from '@angular/common';
import { Component, computed, DestroyRef, inject, PLATFORM_ID, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HotelAdminStore } from '../../core/demo/hotel-admin.store';
import { chartBuckets, entriesForPeriod, ReportPeriod, todayKey } from '../../core/demo/hotel-analytics';
import { DemoRoom, HotelDemoStore } from '../../core/demo/hotel-demo.store';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, AnimatedNumberComponent, TiltDirective, TooltipDirective, DonutChartComponent, BarChartComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  readonly store = inject(HotelDemoStore);
  readonly admin = inject(HotelAdminStore);
  private readonly destroyRef = inject(DestroyRef);
  readonly periods: ReportPeriod[] = ['Hoy', 'Esta semana', 'Este mes'];
  readonly view = inject(AnalyticsViewService);
  readonly auth = inject(AuthService);
  readonly selectedPeriod = this.view.dashboardPeriod;
  readonly revenueOpen = signal(false);
  readonly incomeToday = computed(() => entriesForPeriod(this.admin.ledger(), 'Hoy').filter(row => row.amount > 0));
  readonly barData = computed(() => this.buckets().map(row => ({ label: row.label, values: [row.hours, row.night] })));
  readonly profitData = computed(() => this.roomProfit().map(row => ({ name: row.name, value: row.profit, color: row.color })));
  readonly profitDetail = computed(() => entriesForPeriod(this.admin.ledger(), this.selectedPeriod())
    .filter(row => row.kind === 'Alojamiento' && row.roomType === this.view.selectedType()));
  readonly now = signal(Date.now());
  readonly dateLabel = new Intl.DateTimeFormat('es-PE', { dateStyle: 'full' }).format(new Date());
  readonly todayRevenue = computed(() => this.incomeToday().reduce((sum, row) => sum + row.amount, 0));
  readonly yesterdayRevenue = computed(() => {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    const key = `${yesterday.getFullYear()}-${String(yesterday.getMonth() + 1).padStart(2, '0')}-${String(yesterday.getDate()).padStart(2, '0')}`;
    return this.admin
      .ledger()
      .filter((row) => row.date === key && row.amount > 0)
      .reduce((sum, row) => sum + row.amount, 0);
  });
  readonly revenueChange = computed(() =>
    this.yesterdayRevenue()
      ? Math.round(
          ((this.todayRevenue() - this.yesterdayRevenue()) / this.yesterdayRevenue()) * 100,
        )
      : 0,
  );
  readonly hoursInUse = computed(
    () =>
      this.store.rooms().filter((room) => room.status === 'Ocupada' && room.stay?.mode === 'Horas')
        .length,
  );
  readonly nightInUse = computed(
    () =>
      this.store.rooms().filter((room) => room.status === 'Ocupada' && room.stay?.mode !== 'Horas')
        .length,
  );
  readonly todayStays = computed(
    () =>
      this.admin.ledger().filter((row) => row.date === todayKey() && row.kind === 'Alojamiento')
        .length,
  );
  readonly buckets = computed(() => chartBuckets(this.admin.ledger(), this.selectedPeriod()));
  readonly maxBar = computed(() =>
    Math.max(1, ...this.buckets().map((bucket) => Math.max(bucket.hours, bucket.night))),
  );
  readonly expiring = computed(() =>
    this.store
      .rooms()
      .filter(
        (room) =>
          room.status === 'Ocupada' &&
          room.stay?.mode === 'Horas' &&
          Date.parse(room.stay.endsAt) - this.now() < 2 * 3_600_000,
      )
      .sort((a, b) => Date.parse(a.stay!.endsAt) - Date.parse(b.stay!.endsAt)),
  );
  readonly roomProfit = computed(() =>
    this.admin.roomTypes().map((type, index) => {
      const rows = entriesForPeriod(this.admin.ledger(), this.selectedPeriod())
        .filter((row) => row.kind === 'Alojamiento' && row.roomType === type.name);
      return {
        name: type.name,
        profit: Math.max(
          0,
          rows.reduce((sum, row) => sum + row.amount, 0) - rows.length * (type.costPerStay || 0),
        ),
        color: ['#4d73f5', '#39a7e5', '#839bff', '#5b78bd'][index % 4],
      };
    }),
  );
  readonly totalProfit = computed(() =>
    this.roomProfit().reduce((sum, row) => sum + row.profit, 0),
  );
  readonly donutSegments = computed(() => {
    let position = 0;
    const total = this.totalProfit();
    return this.roomProfit().map((item) => {
      const start = position;
      const size = total ? (item.profit / total) * 100 : 0;
      position += size;
      return { ...item, start, size };
    });
  });
  constructor() {
    if (isPlatformBrowser(inject(PLATFORM_ID))) {
      const timer = setInterval(() => this.now.set(Date.now()), 30_000);
      this.destroyRef.onDestroy(() => clearInterval(timer));
    }
  }
  barHeight(value: number) {
    return (value / this.maxBar()) * 100;
  }
  remaining(room: DemoRoom) {
    const minutes = Math.ceil((Date.parse(room.stay?.endsAt || '') - this.now()) / 60_000);
    return minutes > 0 ? `${minutes} min restantes` : `${Math.abs(minutes)} min vencidos`;
  }
  chartLabel() {
    return this.buckets()
      .map(
        (row) =>
          `${row.label}: ${this.admin.currencySymbol()} ${row.hours} por horas y ${this.admin.currencySymbol()} ${row.night} por noche`,
      )
      .join('; ');
  }
  yieldLabel() {
    return this.roomProfit()
      .map((row) => `${row.name}: ${this.admin.currencySymbol()} ${row.profit} de margen`)
      .join('; ');
  }
  addHour(room: DemoRoom) {
    this.store.extendStay(room.id, 1);
    this.admin.addLedger({
      description: `Hora extra habitación ${room.number}`,
      kind: 'Alojamiento',
      amount: this.admin.ratesFor(room).extraHour,
      method: room.stay?.paymentMethod || 'Efectivo',
      room: room.number,
      roomType: room.type,
      stayMode: 'Horas',
    });
  }
  finish(room: DemoRoom) {
    this.store.finishStay(room.id);
  }
}
