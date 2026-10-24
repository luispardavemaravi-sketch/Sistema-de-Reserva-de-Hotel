import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { signal } from '@angular/core';
import { InicioComponent } from './inicio.component';
import { AuthService } from '../../core/services/auth/auth.service';

describe('InicioComponent', () => {
  it('greets the signed-in user and shows only permitted shortcuts', async () => {
    await TestBed.configureTestingModule({
      imports: [InicioComponent],
      providers: [provideRouter([]), {
        provide: AuthService,
        useValue: { currentUser: signal({ nombres: 'María' }), canAccess: (path: string) => path === 'finanzas' },
      }],
    }).compileComponents();
    const fixture = TestBed.createComponent(InicioComponent);
    fixture.detectChanges();
    const root: HTMLElement = fixture.nativeElement;
    expect(root.querySelector('h1')?.textContent).toContain('María');
    const links = root.querySelectorAll<HTMLAnchorElement>('a.shortcut');
    expect(links.length).toBe(1);
    expect(links[0].getAttribute('href')).toBe('/finanzas');
    expect(root.querySelector('summary')?.textContent).toContain('Guía de inicio');
  });
});
