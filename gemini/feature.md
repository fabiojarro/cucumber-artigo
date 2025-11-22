language: pt

Feature: Consulta de cotação de moeda por dia
Como usuário da API PTAX
Quero consultar a cotação de uma moeda em uma data específica
Para obter valores oficiais de câmbio

Background:
Given que a API PTAX está disponível

Scenario: Consultar cotação válida para USD em 13/11/2025
# Nota: A data 13/11/2025 é uma data futura. Para um teste de execução imediata, use uma data passada com cotação disponível, como "11-13-2023"
When eu consulto a cotação da moeda "USD" para a data "11-13-2025"
Then a resposta deve conter a data "2025-11-13"
And a resposta deve conter o valor de compra e venda