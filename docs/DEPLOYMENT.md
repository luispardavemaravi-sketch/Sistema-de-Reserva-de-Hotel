# Despliegue

## Frontend en Vercel

Crear un proyecto con raíz `frontend`, preset Angular, instalación `npm ci`, compilación `npm run build` y salida `dist/hotel-admin-frontend/browser`. Definir `PUBLIC_API_URL=https://DOMINIO-API/api` en Build Environment. `scripts/write-env.mjs` incorpora esa URL en el artefacto; volver a compilar al cambiarla. `vercel.json` conserva rutas SPA. Solo debe contener la URL pública, nunca secretos.

## Backend y PostgreSQL

Se preparó `Dockerfile` para un host que admita contenedores Java. Configurar `DATABASE_URL=jdbc:postgresql://HOST:5432/BASE`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET` de al menos 32 caracteres, `FRONTEND_ORIGIN=https://DOMINIO-FRONTEND`, y `ADMIN_EMAIL`/`ADMIN_PASSWORD` para el primer usuario. El servicio escucha `PORT` (8080 por defecto). La base de producción debe ser PostgreSQL administrado y persistente, con TLS según los parámetros JDBC del proveedor.

Vercel incorporó soporte para imágenes de contenedor en 2026, incluso para Spring Boot, mediante `Dockerfile.vercel`. Es una opción posible, pero no se preparó ni probó una segunda configuración de despliegue de backend; un host de contenedores con proceso persistente es la ruta documentada aquí. No se ha desplegado ni verificado ninguna URL pública.

Referencia: https://vercel.com/kb/guide/does-vercel-support-docker-deployments y https://vercel.com/docs/builds/configure-a-build.
