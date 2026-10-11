// Seed executado automaticamente na 1ª subida do container Mongo.
// Campos em snake_case (mesma convenção da atividade MongoDB ESG); dados flexíveis em "atributos".
const dbEsg = db.getSiblingDB("esg_energy_db");

["unidades_operacionais", "dispositivos_iot", "leituras_consumo",
 "incidentes_alertas", "acoes_eficiencia"].forEach(c => dbEsg.createCollection(c));

dbEsg.unidades_operacionais.insertMany([
  { codigo: "UN-01", nome: "Fábrica Central - Mooca", tipo: "Industrial", meta_reducao_anual_pct: 12.5,
    atributos: { gestor_esg: { nome: "Fernanda Costa" }, certificacoes: ["ISO 50001", "ISO 14001"] } },
  { codigo: "UN-02", nome: "Centro de Distribuição Cajamar", tipo: "Logística", meta_reducao_anual_pct: 8.0,
    atributos: { painel_solar_instalado: true, capacidade_geracao_kwh: 450 } },
  { codigo: "UN-09", nome: "Data Center Alphaville", tipo: "Tecnologia", meta_reducao_anual_pct: 18.0,
    atributos: { pue_alvo: 1.25 } }
]);

dbEsg.dispositivos_iot.insertMany([
  { mac_address: "AA:BB:CC:11:00:01", modelo: "PowerMeter Smart 3000", unidade_codigo: "UN-01",
    tipo: "Medidor Trifásico", status: "Ativo", limite_alerta_kwh: 120.0,
    atributos: { tensao_nominal: 380, precisao: 0.2, protocolo: "Modbus/TCP" } },
  { mac_address: "AA:BB:CC:11:00:03", modelo: "SolarSensor-Lux", unidade_codigo: "UN-02",
    tipo: "Inversor Solar", status: "Ativo", limite_alerta_kwh: 300.0,
    atributos: { area_placas_m2: 1200, potencia_pico_kwp: 250 } },
  { mac_address: "AA:BB:CC:11:00:09", modelo: "RackPower-DC", unidade_codigo: "UN-09",
    tipo: "Medidor Trifásico", status: "Ativo", limite_alerta_kwh: 85.0,
    atributos: { pdu_conectado: "PDU-Rack-A12", tensao_nominal: 220 } }
]);

dbEsg.leituras_consumo.insertMany([
  { mac_dispositivo: "AA:BB:CC:11:00:01", timestamp: ISODate("2026-03-04T08:00:00Z"), consumo_kwh: 125.4, emissao_estimada_co2_kg: 5.26, atributos: { tensao_v: 381.2, fator_potencia: 0.94 } },
  { mac_dispositivo: "AA:BB:CC:11:00:01", timestamp: ISODate("2026-03-04T09:00:00Z"), consumo_kwh: 138.9, emissao_estimada_co2_kg: 5.83, atributos: { tensao_v: 379.8, fator_potencia: 0.92 } },
  { mac_dispositivo: "AA:BB:CC:11:00:03", timestamp: ISODate("2026-03-04T12:00:00Z"), consumo_kwh: -185.0, atributos: { tipo_medicao: "Geracao_Solar", co2_evitado_kg: 12.4 } },
  { mac_dispositivo: "AA:BB:CC:11:00:09", timestamp: ISODate("2026-03-04T10:00:00Z"), consumo_kwh: 82.3, emissao_estimada_co2_kg: 3.45, atributos: { fator_potencia: 0.99 } },
  { mac_dispositivo: "AA:BB:CC:11:00:09", timestamp: ISODate("2026-03-04T11:00:00Z"), consumo_kwh: 88.6, emissao_estimada_co2_kg: 3.72, atributos: { fator_potencia: 0.99 } }
]);

dbEsg.incidentes_alertas.insertMany([
  { protocolo: "ALT-2026-001", mac_dispositivo: "AA:BB:CC:11:00:01", nivel_severidade: "ALTA",
    descricao: "Consumo acima do teto contratado em horário de ponta", status: "Resolvido",
    data_ocorrencia: ISODate("2026-03-04T09:05:00Z"),
    atributos: { acao_mitigadora: { acao: "Corte automático de circuitos secundários", automatico: true } } },
  { protocolo: "ALT-2026-003", mac_dispositivo: "AA:BB:CC:11:00:09", nivel_severidade: "MEDIA",
    descricao: "Pico transitório de consumo nos servidores do Rack A12", status: "Em Analise",
    data_ocorrencia: ISODate("2026-03-04T11:02:00Z"), atributos: { atribuido_para: "Patrícia Melo" } }
]);

dbEsg.acoes_eficiencia.insertMany([
  { codigo_projeto: "PRJ-ESG-01", titulo: "Retrofit de Iluminação Industrial para LED Smart", unidade_alvo: "UN-01",
    investimento_brl: 45000.0, economia_esperada_anual_kwh: 65000.0, status: "Concluido", progresso_pct: 100,
    atributos: { equipes_engajadas: ["Manutenção", "Produção Linha A"] } },
  { codigo_projeto: "PRJ-ESG-02", titulo: "Implementação de Painéis Solares no Galpão 3", unidade_alvo: "UN-02",
    investimento_brl: 320000.0, economia_esperada_anual_kwh: 280000.0, status: "Em Execucao", progresso_pct: 65, atributos: {} }
]);
