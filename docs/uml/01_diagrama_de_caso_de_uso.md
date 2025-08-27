graph LR
    subgraph "Sistema: Módulo de Atendimento e Cliente"
        Cliente(("Cliente"))
        Cliente --> uc01(Ver Cardápio)
        Cliente --> uc02("Criar / Editar Comanda (antes do envio do pedido)")
        Cliente --> uc03(Realizar Pedido)

        Garcom(("Garçom"))
        Garcom --> uc04(Visualizar Estado das Mesas)
        Garcom --> uc05(Gerenciar Pedidos do Salão)
        Garcom --> uc06(Fechar Comanda e Pagamento)
        Garcom --> uc07(Liberar Mesa)
        Garcom --> uc_ext1(Aplicar Desconto)

        %% --- Relações entre Casos de Uso ---
        uc03 -.-> uc02 
        uc_ext1 -.-> uc06 
    end

    subgraph "Sistema: Módulo de Gestão e Cozinha"
        Cozinheiro(("Cozinheiro"))
        Cozinheiro --> uc08(Visualizar Fila de Preparo)
        Cozinheiro --> uc09(Atualizar Status do Pedido)
        Cozinheiro --> uc10(Gerenciar Cardápio e Receitas)

        Admin(("Admin"))
        Admin --> uc10
        Admin --> uc11(Gerenciar Usuários)
        Admin --> uc12(Gerenciar Mesas)
        Admin --> uc13(Ver Dashboards de Vendas)
    end
