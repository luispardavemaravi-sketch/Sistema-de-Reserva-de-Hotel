import { isPlatformBrowser } from '@angular/common';
import { Component, computed, DestroyRef, inject, PLATFORM_ID, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HotelAdminStore } from '../../core/demo/hotel-admin.store';
import { chartBuckets, ReportPeriod, todayKey } from '../../core/demo/hotel-analytics';
import { DemoRoom, HotelDemoStore } from '../../core/demo/hotel-demo.store';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink],
  template: ` <section class="page analytics-page">
    <div class="page-head">
      <div>
        <p class="eyebrow">DATOS DE DEMOSTRACIÓN</p>
        <h1>Resumen general</h1>
        <p>{{ admin.profile().name }} · {{ dateLabel }}</p>
      </div>
      <a class="btn btn-primary" routerLink="/recepcion"
        ><i class="pi pi-th-large" aria-hidden="true"></i> Abrir recepción</a
      >
    </div>
    <div class="kpi-grid">
      <article class="kpi featured">
        <div class="kpi-top">
          <span>Ingresos de hoy</span><i class="pi pi-wallet" aria-hidden="true"></i>
        </div>
        <strong>{{ admin.currencySymbol() }} {{ todayRevenue() }}</strong>
        <p>
          <span class="trend" [class.down]="revenueChange() < 0"
            >{{ revenueChange() >= 0 ? '+' : '' }}{{ revenueChange() }}%</span
          >
          frente a ayer
        </p>
      </article>
      <article class="kpi">
        <div class="kpi-top">
          <span>Habitaciones en uso</span><i class="pi pi-key" aria-hidden="true"></i>
        </div>
        <strong>{{ store.occupied() }}</strong>
        <p>{{ hoursInUse() }} por horas · {{ nightInUse() }} por noche</p>
      </article>
      <article class="kpi">
        <div class="kpi-top">
          <span>Rotación del día</span><i class="pi pi-sync" aria-hidden="true"></i>
        </div>
        <strong>{{ todayStays() }}</strong>
        <p>Ocupaciones registradas hoy</p>
      </article>
      <article class="kpi">
        <div class="kpi-top">
          <span>Disponibles</span><i class="pi pi-check-circle" aria-hidden="true"></i>
        </div>
        <strong>{{ store.available() }}</strong>
        <p>{{ store.cleaning() }} en limpieza · {{ store.rooms().length }} habitaciones</p>
      </article>
    </div>
    <div class="dashboard-grid">
      <section class="card chart-card">
        <div class="section-heading">
          <div>
            <h2>Ingresos por modalidad</h2>
            <p>Alquileres por horas y por noche</p>
          </div>
          <div class="segmented" role="group" aria-label="Periodo del gráfico">
            @for (period of periods; track period) {
              <button
                type="button"
                [class.active]="selectedPeriod() === period"
                [attr.aria-pressed]="selectedPeriod() === period"
                (click)="selectedPeriod.set(period)"
              >
                {{ period }}
              </button>
            }
          </div>
        </div>
        <div class="chart-legend">
          <span><i class="legend-hour"></i> Por horas</span
          ><span><i class="legend-night"></i> Por noche</span>
        </div>
        <div class="bar-chart" role="img" [attr.aria-label]="chartLabel()">
          @for (bucket of buckets(); track bucket.label) {
            <div class="chart-group">
              <div class="bar-pair">
                <div
                  class="bar hour"
                  [style.height.%]="barHeight(bucket.hours)"
                  [attr.title]="
                    bucket.label + ': ' + admin.currencySymbol() + bucket.hours + ' por horas'
                  "
                ></div>
                <div
                  class="bar night"
                  [style.height.%]="barHeight(bucket.night)"
                  [attr.title]="
                    bucket.label + ': ' + admin.currencySymbol() + bucket.night + ' por noche'
                  "
                ></div>
              </div>
              <span>{{ bucket.label }}</span>
            </div>
          }
        </div>
      </section>
      <section class="card yield-card">
        <div class="section-heading">
          <div>
            <h2>Rentabilidad por tipo</h2>
            <p>Ingresos de alojamiento menos costo por estadía</p>
          </div>
        </div>
        <div class="yield-layout">
          <div class="donut" role="img" [attr.aria-label]="yieldLabel()">
            <svg viewBox="0 0 100 100" aria-hidden="true">
              <circle cx="50" cy="50" r="40" fill="none" stroke="currentColor" stroke-width="14" />
              @for (segment of donutSegments(); track segment.name) {
                <circle
                  cx="50"
                  cy="50"
                  r="40"
                  fill="none"
                  [attr.stroke]="segment.color"
                  stroke-width="14"
                  pathLength="100"
                  [attr.stroke-dasharray]="segment.size + ' ' + (100 - segment.size)"
                  [attr.stroke-dashoffset]="-segment.start"
                />
              }
            </svg>
            <div>
              <strong>{{ admin.currencySymbol() }} {{ totalProfit() }}</strong
              ><small>Margen estimado</small>
            </div>
          </div>
          <div class="yield-list">
            @for (item of roomProfit(); track item.name) {
              <div class="yield-row">
                <span><i [style.background]="item.color"></i>{{ item.name }}</span
                ><strong>{{ admin.currencySymbol() }} {{ item.profit }}</strong>
              </div>
            }
          </div>
        </div>
      </section>
    </div>
    <div class="dashboard-grid lower">
      <section class="card">
        <div class="section-heading">
          <div>
            <h2>Mapa rápido</h2>
            <p>Estado actual de cada habitación</p>
          </div>
          <a routerLink="/recepcion" class="text-link"
            >Ver rack completo <i class="pi pi-arrow-right" aria-hidden="true"></i
          ></a>
        </div>
        <div class="mini-rack">
          @for (room of store.rooms(); track room.id) {
            <a routerLink="/recepcion" [class]="'mini-room state-' + room.status.toLowerCase()"
              ><strong>{{ room.number }}</strong
              ><span>{{ room.status }}</span></a
            >
          }
        </div>
        <div class="chart-legend">
          <span><i class="legend-available"></i> Disponible</span
          ><span><i class="legend-occupied"></i> Ocupada</span
          ><span><i class="legend-cleaning"></i> Limpieza</span
          ><span><i class="legend-maintenance"></i> Mantenimiento</span>
        </div>
      </section>
      <section class="card">
        <div class="section-heading">
          <div>
            <h2>Habitaciones por vencer</h2>
            <p>Estadías por horas con salida próxima</p>
          </div>
          <span class="badge badge-amber">{{ expiring().length }} activas</span>
        </div>
        @for (room of expiring(); track room.id) {
          <div class="due-row">
            <div>
              <strong>Hab. {{ room.number }}</strong
              ><small>{{ room.guest }} · {{ room.stay?.durationHours }} horas</small>
            </div>
            <span class="due-time">{{ remaining(room) }}</span>
            <div class="due-actions">
              <button class="btn" (click)="addHour(room)">+ Hora extra</button
              ><button class="btn" (click)="finish(room)">Finalizar y liberar</button>
            </div>
          </div>
        } @empty {
          <p class="empty">No hay habitaciones por horas próximas a vencer.</p>
        }
      </section>
    </div>
  </section>`,
  styles: [
    `
      .analytics-page {
        --night: #4d73f5;
        --hour: #39a7e5;
      }
      .page-head .eyebrow {
        font-size: 13px;
        letter-spacing: 0.1em;
        font-weight: 600;
        color: #385ba8;
        margin: 0 0 7px;
      }
      .kpi-grid {
        display: grid;
        grid-template-columns: repeat(4, minmax(0, 1fr));
        gap: 14px;
        margin-bottom: 20px;
      }
      .kpi {
        background: var(--surface, #fff);
        border: 1px solid var(--line, #e1e8eb);
        border-radius: 10px;
        padding: 19px;
        min-height: 146px;
      }
      .kpi.featured {
        background: #1b3158;
        color: white;
        border-color: #4164b7;
      }
      .kpi-top {
        display: flex;
        justify-content: space-between;
        gap: 10px;
        font-size: 13px;
        color: var(--muted, #5c6b76);
      }
      .featured .kpi-top {
        color: #c3dce5;
      }
      .kpi-top i {
        font-size: 17px;
      }
      .kpi strong {
        display: block;
        font-size: 30px;
        font-weight: 600;
        margin-top: 17px;
        font-variant-numeric: tabular-nums;
      }
      .kpi p {
        font-size: 13px;
        color: var(--muted, #5c6b76);
        margin: 6px 0 0;
      }
      .featured p {
        color: #d1e2e8;
      }
      .trend {
        color: #2c8650;
        font-weight: 700;
      }
      .featured .trend {
        color: #b1efc5;
      }
      .trend.down {
        color: #bd3945;
      }
      .dashboard-grid {
        display: grid;
        grid-template-columns: minmax(0, 1.55fr) minmax(310px, 1fr);
        gap: 18px;
        margin-bottom: 18px;
      }
      .section-heading {
        display: flex;
        justify-content: space-between;
        align-items: start;
        gap: 15px;
      }
      .section-heading h2 {
        font-size: 16px;
        margin: 0;
      }
      .section-heading p {
        font-size: 13px;
        color: var(--muted, #62717c);
        margin: 5px 0 0;
      }
      .segmented {
        display: flex;
        border: 1px solid var(--line, #d9e3e8);
        border-radius: 7px;
        overflow: hidden;
      }
      .segmented button {
        background: transparent;
        border: 0;
        color: var(--muted, #52636e);
        padding: 7px 9px;
        font-size: 13px;
      }
      .segmented button.active {
        background: #345bd7;
        color: #fff;
      }
      .chart-legend {
        display: flex;
        flex-wrap: wrap;
        gap: 15px;
        font-size: 13px;
        color: var(--muted, #5c6b76);
        margin: 20px 0 0;
      }
      .chart-legend i,
      .yield-row i {
        display: inline-block;
        width: 9px;
        height: 9px;
        border-radius: 2px;
        margin-right: 6px;
      }
      .legend-hour {
        background: #39a7e5;
      }
      .legend-night {
        background: #4d73f5;
      }
      .legend-available {
        background: #5c9d51;
      }
      .legend-occupied {
        background: #a23838;
      }
      .legend-cleaning {
        background: #e3bd74;
      }
      .legend-maintenance {
        background: #aab3ba;
      }
      .bar-chart {
        height: 245px;
        display: flex;
        gap: 10px;
        align-items: end;
        border-bottom: 1px solid var(--line, #dde6ea);
        margin-top: 14px;
        padding-top: 10px;
      }
      .chart-group {
        flex: 1;
        min-width: 0;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: end;
        align-items: center;
      }
      .bar-pair {
        height: 205px;
        width: 100%;
        max-width: 75px;
        display: flex;
        align-items: end;
        justify-content: center;
        gap: 4px;
      }
      .bar {
        width: 40%;
        min-height: 1px;
        border-radius: 4px 4px 0 0;
        transition: height 0.25s;
      }
      .bar.hour {
        background: #39a7e5;
      }
      .bar.night {
        background: #4d73f5;
      }
      .chart-group > span {
        font-size: 12px;
        color: var(--muted, #61717a);
        margin: 8px 0 5px;
      }
      .yield-layout {
        display: grid;
        grid-template-columns: 150px 1fr;
        gap: 20px;
        align-items: center;
        min-height: 285px;
      }
      .donut {
        width: 150px;
        height: 150px;
        border-radius: 50%;
        display: grid;
        place-items: center;
      }
      .donut > div {
        width: 104px;
        height: 104px;
        border-radius: 50%;
        background: var(--surface, #fff);
        display: grid;
        align-content: center;
        text-align: center;
      }
      .donut strong {
        font-size: 18px;
      }
      .donut small {
        font-size: 12px;
        color: var(--muted, #65747e);
      }
      .yield-list {
        min-width: 0;
      }
      .yield-row {
        display: flex;
        justify-content: space-between;
        gap: 9px;
        padding: 11px 0;
        border-bottom: 1px solid var(--line, #e6ecee);
        font-size: 13px;
      }
      .yield-row span {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .yield-row strong {
        font-variant-numeric: tabular-nums;
      }
      .mini-rack {
        display: grid;
        grid-template-columns: repeat(6, 1fr);
        gap: 7px;
        margin-top: 20px;
      }
      .mini-room {
        border: 1px solid var(--line, #e1e8eb);
        border-top: 4px solid #aab3ba;
        border-radius: 5px;
        padding: 9px;
        color: inherit;
        display: grid;
        gap: 3px;
        font-size: 12px;
      }
      .mini-room strong {
        font-size: 14px;
      }
      .mini-room span {
        color: var(--muted, #62717c);
      }
      .mini-room.state-disponible {
        border-top-color: #5c9d51;
      }
      .mini-room.state-ocupada {
        border-top-color: #a23838;
      }
      .mini-room.state-limpieza {
        border-top-color: #e3bd74;
      }
      .due-row {
        display: grid;
        grid-template-columns: 1fr auto;
        gap: 8px;
        padding: 14px 0;
        border-bottom: 1px solid var(--line, #e5ebee);
      }
      .due-row strong,
      .due-row small {
        display: block;
      }
      .due-row small {
        font-size: 13px;
        color: var(--muted, #60717b);
        margin-top: 4px;
      }
      .due-time {
        color: #a23838;
        font-weight: 600;
        font-size: 13px;
      }
      .due-actions {
        grid-column: 1/-1;
        display: flex;
        gap: 7px;
      }
      .due-actions .btn {
        font-size: 12px;
        min-height: 30px;
      }
      .text-link {
        color: #466de6;
        font-size: 13px;
        font-weight: 600;
      }
      @media (max-width: 1100px) {
        .kpi-grid {
          grid-template-columns: repeat(2, 1fr);
        }
        .dashboard-grid {
          grid-template-columns: 1fr;
        }
      }
      @media (max-width: 600px) {
        .kpi-grid {
          gap: 8px;
        }
        .kpi {
          padding: 13px;
          min-height: 135px;
        }
        .kpi strong {
          font-size: 24px;
        }
        .section-heading {
          flex-wrap: wrap;
        }
        .yield-layout {
          grid-template-columns: 1fr;
          justify-items: center;
          padding-top: 18px;
        }
        .yield-list {
          width: 100%;
        }
        .mini-rack {
          grid-template-columns: repeat(3, 1fr);
        }
      }
      .donut {
        position: relative;
      }
      .donut svg {
        width: 100%;
        height: 100%;
        color: var(--line);
        transform: rotate(-90deg);
      }
      .donut > div {
        position: absolute;
      }
      .kpi-grid,
      .chart-group,
      .yield-row,
      .mini-rack,
      .due-row {
        font-variant-numeric: tabular-nums;
      }
    `,
  ],
})
export class DashboardComponent {
  readonly store = inject(HotelDemoStore);
  readonly admin = inject(HotelAdminStore);
  private readonly destroyRef = inject(DestroyRef);
  readonly periods: ReportPeriod[] = ['Hoy', 'Esta semana', 'Este mes'];
  readonly selectedPeriod = signal<ReportPeriod>('Esta semana');
  readonly now = signal(Date.now());
  readonly dateLabel = new Intl.DateTimeFormat('es-PE', { dateStyle: 'full' }).format(new Date());
  readonly todayRevenue = computed(() => this.admin.todayRevenue());
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
      const rows = this.admin
        .ledger()
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
