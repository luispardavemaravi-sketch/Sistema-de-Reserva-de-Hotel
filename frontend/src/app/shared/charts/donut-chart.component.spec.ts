import { TestBed } from '@angular/core/testing';
import { DonutChartComponent, ringPath } from './donut-chart.component';

describe('Interactive donut', () => {
  it('keeps proportions, allows pin/unpin and handles empty data', () => {
    const fixture = TestBed.createComponent(DonutChartComponent);
    fixture.componentRef.setInput('data', [
      { name: 'A', value: 30, color: '#547af8' }, { name: 'B', value: 70, color: '#47ade5' },
    ]);
    fixture.detectChanges();
    expect(fixture.componentInstance.segments().map(row => row.percent)).toEqual([30, 70]);
    const selections: (string | null)[] = [];
    fixture.componentInstance.selectionChange.subscribe(value => selections.push(value));
    const first: SVGGElement = fixture.nativeElement.querySelector('g');
    first.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));
    expect(selections).toEqual(['A']);
    fixture.componentRef.setInput('selected', 'A');
    fixture.detectChanges();
    first.dispatchEvent(new MouseEvent('click'));
    expect(selections).toEqual(['A', null]);
    fixture.componentRef.setInput('data', []);
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('No hay importes');
    expect(ringPath(0, 1)).not.toContain('NaN');
  });
});
