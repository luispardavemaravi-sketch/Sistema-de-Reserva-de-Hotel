import { Component, input, signal, effect, inject, computed } from '@angular/core';
import { MotionPreferences } from './motion-preferences.service';

@Component({
  selector: 'app-animated-number',
  template: `<span aria-hidden="true">{{ prefix() }}{{ formatted() }}</span><span class="sr-only">{{ prefix() }}{{ value().toLocaleString('es-PE', { minimumFractionDigits: decimals(), maximumFractionDigits: decimals() }) }}</span>`,
  host: { style: 'font-variant-numeric: tabular-nums' },
})
export class AnimatedNumberComponent {
  readonly value = input.required<number>();
  readonly prefix = input('');
  readonly decimals = input(0);
  private readonly motion = inject(MotionPreferences);
  readonly shown = signal(0);
  readonly formatted = computed(() => this.shown().toLocaleString('es-PE', {
    minimumFractionDigits: this.decimals(), maximumFractionDigits: this.decimals(),
  }));
  constructor() {
    effect(onCleanup => {
      const target = this.value();
      if (this.motion.reduced() || typeof requestAnimationFrame === 'undefined') { this.last = target; this.shown.set(target); return; }
      const start = performance.now();
      // Read the current visual value without making it an effect dependency.
      const from = this.last;
      let frame = 0;
      const tick = (now: number) => {
        const progress = Math.min(1, (now - start) / 450);
        this.last = from + (target - from) * (1 - (1 - progress) ** 3);
        this.shown.set(progress === 1 ? target : this.last);
        if (progress < 1) frame = requestAnimationFrame(tick);
      };
      frame = requestAnimationFrame(tick);
      onCleanup(() => cancelAnimationFrame(frame));
    });
  }
  private last = 0;
}
