import { Directive, ElementRef, NgZone, DestroyRef, inject, effect } from '@angular/core';
import { MotionPreferences } from './motion-preferences.service';

/** One cached measurement per hover; pointer frames never update Angular state. */
@Directive({ selector: '[pardaTilt], .kpi, .metric, .shift-card', host: { class: 'parda-tilt' } })
export class TiltDirective {
  private readonly el = inject<ElementRef<HTMLElement>>(ElementRef).nativeElement;
  private readonly motion = inject(MotionPreferences);
  constructor() {
    const destroy = inject(DestroyRef);
    let frame = 0;
    let bounds: DOMRect | null = null;
    const reset = () => {
      cancelAnimationFrame(frame);
      frame = 0;
      bounds = null;
      this.el.style.removeProperty('transform');
    };
    effect(() => { if (this.motion.reduced() || !this.motion.finePointer()) reset(); });
    inject(NgZone).runOutsideAngular(() => {
      const enter = () => { bounds = this.el.getBoundingClientRect(); };
      const move = (event: PointerEvent) => {
        if (!bounds || this.motion.reduced() || !this.motion.finePointer() || event.pointerType === 'touch') return;
        cancelAnimationFrame(frame);
        const x = Math.max(-.5, Math.min(.5, (event.clientX - bounds.left) / bounds.width - .5));
        const y = Math.max(-.5, Math.min(.5, (event.clientY - bounds.top) / bounds.height - .5));
        frame = requestAnimationFrame(() => {
          this.el.style.transform = `perspective(900px) translateY(-4px) rotateX(${-y * 6}deg) rotateY(${x * 6}deg)`;
        });
      };
      this.el.addEventListener('pointerenter', enter);
      this.el.addEventListener('pointermove', move, { passive: true });
      this.el.addEventListener('pointerleave', reset);
      this.el.addEventListener('pointercancel', reset);
      destroy.onDestroy(() => {
        reset();
        this.el.removeEventListener('pointerenter', enter);
        this.el.removeEventListener('pointermove', move);
        this.el.removeEventListener('pointerleave', reset);
        this.el.removeEventListener('pointercancel', reset);
      });
    });
  }
}
