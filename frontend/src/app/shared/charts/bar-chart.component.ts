import { Component, input, computed } from '@angular/core';
import { TooltipDirective } from '../motion/tooltip.directive';

export interface BarDatum { label: string; values: number[]; }
@Component({
  selector: 'app-bar-chart',
  imports: [TooltipDirective],
  template: `
    @if (hasData()) {
      <div class="interactive-bars" role="group" [attr.aria-label]="label()">
        @for (bucket of data(); track bucket.label; let index = $index) {
          <div class="interactive-bar-group">
            <div class="interactive-bar-pair">
              @for (value of bucket.values; track $index; let series = $index) {
                <button type="button" class="bar-target" [pardaTooltip]="description(bucket, series)" [attr.aria-label]="description(bucket, series)">
                  <span class="bar-fill" aria-hidden="true" [style.background]="colors()[series % colors().length]"
                    [style.transform]="'scaleY(' + Math.max(0, value) / maximum() + ')'"
                    [style.animation-delay.ms]="Math.min(index * 30, 150)"></span>
                </button>
              }
            </div>
            <span class="bar-label">{{ bucket.label }}</span>
          </div>
        }
      </div>
      <div class="interactive-bar-legend">
        @for (name of series(); track name; let index = $index) {
          <span><i [style.background]="colors()[index % colors().length]" aria-hidden="true"></i>{{ name }}</span>
        }
      </div>
    } @else { <div class="chart-empty" role="status"><i class="pi pi-chart-bar" aria-hidden="true"></i><p>No hay movimientos para este período.</p></div> }
  `,
})
export class BarChartComponent {
  readonly data = input.required<BarDatum[]>();
  readonly series = input.required<string[]>();
  readonly colors = input(['#547af8', '#47ade5']);
  readonly currency = input('S/');
  readonly label = input('Importes por período');
  readonly Math = Math;
  readonly maximum = computed(() => Math.max(1, ...this.data().flatMap(row => row.values)));
  readonly hasData = computed(() => this.data().some(row => row.values.some(value => value > 0)));
  description(row: BarDatum, series: number) {
    return `${row.label} · ${this.series()[series]}: ${this.currency()} ${row.values[series].toLocaleString('es-PE', { maximumFractionDigits: 2 })}`;
  }
}
