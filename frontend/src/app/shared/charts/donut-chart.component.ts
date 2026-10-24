import { Component, computed, input, output, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { TooltipDirective } from '../motion/tooltip.directive';

export interface DonutDatum { name: string; value: number; color: string; }

export function ringPath(start: number, fraction: number): string {
  const angle = Math.max(0, Math.min(fraction, .999999)) * Math.PI * 2;
  const a = start * Math.PI * 2 - Math.PI / 2;
  const point = (r: number, t: number) => `${60 + r * Math.cos(t)},${60 + r * Math.sin(t)}`;
  return `M${point(46, a)} A46,46 0 ${angle > Math.PI ? 1 : 0},1 ${point(46, a + angle)} L${point(31, a + angle)} A31,31 0 ${angle > Math.PI ? 1 : 0},0 ${point(31, a)} Z`;
}

@Component({
  selector: 'app-donut-chart',
  imports: [DecimalPipe, TooltipDirective],
  template: `
    @if (total() > 0) {
      <div class="interactive-donut-layout">
        <div class="interactive-donut">
          <svg viewBox="0 0 120 128" role="group" [attr.aria-label]="label()">
            @for (part of segments(); track part.name) {
              <g role="button" tabindex="0" [attr.aria-label]="description(part)" [pardaTooltip]="description(part)"
                [attr.aria-pressed]="selected() === part.name" [class.segment-active]="active()?.name === part.name"
                (pointerenter)="hovered.set(part.name)" (pointerleave)="hovered.set(null)"
                (focus)="hovered.set(part.name)" (blur)="hovered.set(null)"
                (click)="toggle(part.name)" (keydown.enter)="toggle(part.name)"
                (keydown.space)="$event.preventDefault(); toggle(part.name)" (keydown.escape)="clear()"
                [style.transform]="active()?.name === part.name ? part.explode : 'translate(0px, 0px)'">
                <path class="segment-depth" [attr.d]="part.path" [attr.fill]="part.color" transform="translate(0 6)" />
                <path class="segment-face" [attr.d]="part.path" [attr.fill]="part.color" />
              </g>
            }
          </svg>
          <div class="donut-center" aria-hidden="true">
            <strong>{{ currency() }} {{ (active()?.value ?? total()) | number:'1.0-2' }}</strong>
            <small>{{ active()?.name ?? centerLabel() }}</small>
            @if (active(); as part) { <span>{{ part.percent | number:'1.1-1' }}%</span> }
          </div>
        </div>
        <div class="donut-legend">
          @for (part of segments(); track part.name) {
            <button type="button" [class.selected]="selected() === part.name"
              [attr.aria-pressed]="selected() === part.name" [pardaTooltip]="description(part)"
              (pointerenter)="hovered.set(part.name)" (pointerleave)="hovered.set(null)"
              (focus)="hovered.set(part.name)" (blur)="hovered.set(null)" (click)="toggle(part.name)">
              <span><i [style.background]="part.color" aria-hidden="true"></i>{{ part.name }}</span>
              <strong>{{ currency() }} {{ part.value | number:'1.0-2' }}<small>{{ part.percent | number:'1.1-1' }}%</small></strong>
            </button>
          }
          <button class="chart-clear" type="button" [disabled]="!selected()" (click)="clear()">Quitar selección</button>
        </div>
      </div>
      <p class="chart-readout" aria-live="polite">{{ active() ? description(active()!) : 'Pasa el cursor, enfoca o toca un segmento para explorar.' }}</p>
    } @else {
      <p class="empty" role="status">No hay importes positivos para este período.</p>
    }
  `,
})
export class DonutChartComponent {
  readonly data = input.required<DonutDatum[]>();
  readonly currency = input('S/');
  readonly label = input('Distribución de importes');
  readonly centerLabel = input('Total');
  readonly selected = input<string | null>(null);
  readonly selectionChange = output<string | null>();
  readonly hovered = signal<string | null>(null);
  readonly total = computed(() => this.data().reduce((sum, row) => sum + Math.max(0, row.value), 0));
  readonly segments = computed(() => {
    let start = 0;
    const total = this.total();
    return this.data().filter(row => row.value > 0).map(row => {
      const fraction = total ? row.value / total : 0;
      const angle = (start + fraction / 2) * Math.PI * 2 - Math.PI / 2;
      const path = ringPath(start, fraction);
      start += fraction;
      return { ...row, path, percent: fraction * 100, explode: `translate(${Math.cos(angle) * 4}px, ${Math.sin(angle) * 4 - 2}px)` };
    });
  });
  readonly active = computed(() => this.segments().find(row => row.name === (this.hovered() ?? this.selected())));
  toggle(name: string) { this.selectionChange.emit(this.selected() === name ? null : name); }
  clear() { this.hovered.set(null); this.selectionChange.emit(null); }
  description(part: DonutDatum & { percent: number }) {
    return `${part.name}: ${this.currency()} ${part.value.toLocaleString('es-PE', { maximumFractionDigits: 2 })} · ${part.percent.toFixed(1)}%`;
  }
}
