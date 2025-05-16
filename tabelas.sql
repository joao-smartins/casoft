--
-- PostgreSQL database dump
--

-- Dumped from database version 15.13
-- Dumped by pg_dump version 16.8

-- Started on 2025-05-16 14:23:47 -03

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 219 (class 1259 OID 16689)
-- Name: categoriadesp; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoriadesp (
    catdesp_id integer NOT NULL,
    catdesp_nome character varying(30) NOT NULL
);


ALTER TABLE public.categoriadesp OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16688)
-- Name: categoriadesp_catdesp_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categoriadesp_catdesp_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categoriadesp_catdesp_id_seq OWNER TO postgres;

--
-- TOC entry 4545 (class 0 OID 0)
-- Dependencies: 218
-- Name: categoriadesp_catdesp_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categoriadesp_catdesp_id_seq OWNED BY public.categoriadesp.catdesp_id;


--
-- TOC entry 221 (class 1259 OID 16696)
-- Name: categoriarec; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoriarec (
    catresc_id integer NOT NULL,
    catresc_nome character varying(30) NOT NULL
);


ALTER TABLE public.categoriarec OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16695)
-- Name: categoriarec_catresc_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categoriarec_catresc_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categoriarec_catresc_id_seq OWNER TO postgres;

--
-- TOC entry 4546 (class 0 OID 0)
-- Dependencies: 220
-- Name: categoriarec_catresc_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categoriarec_catresc_id_seq OWNED BY public.categoriarec.catresc_id;


--
-- TOC entry 229 (class 1259 OID 16846)
-- Name: contabancaria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contabancaria (
    contab_id integer NOT NULL,
    contab_agencia integer NOT NULL,
    contab_numero character varying(10),
    contab_banco character varying(20),
    contab_telefone character varying(20),
    contab_endereco character varying(30),
    contab_ende_num integer,
    contab_gerente character varying(20)
);


ALTER TABLE public.contabancaria OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 16845)
-- Name: contabancaria_contab_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contabancaria_contab_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.contabancaria_contab_id_seq OWNER TO postgres;

--
-- TOC entry 4547 (class 0 OID 0)
-- Dependencies: 228
-- Name: contabancaria_contab_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contabancaria_contab_id_seq OWNED BY public.contabancaria.contab_id;


--
-- TOC entry 225 (class 1259 OID 16761)
-- Name: despesa; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.despesa (
    despesa_id integer NOT NULL,
    despesa_val numeric NOT NULL,
    despesa_dt_venc date NOT NULL,
    despesa_lancamento date NOT NULL,
    despesa_pagamento numeric,
    despesa_desc character varying(100) NOT NULL,
    despesa_statusconci character varying(20),
    categoriadesp_catdesp_id integer NOT NULL,
    usuario_user_id integer NOT NULL,
    evento_evento_id integer NOT NULL
);


ALTER TABLE public.despesa OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16760)
-- Name: despesa_despesa_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.despesa_despesa_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.despesa_despesa_id_seq OWNER TO postgres;

--
-- TOC entry 4548 (class 0 OID 0)
-- Dependencies: 224
-- Name: despesa_despesa_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.despesa_despesa_id_seq OWNED BY public.despesa.despesa_id;


--
-- TOC entry 236 (class 1259 OID 16894)
-- Name: email; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.email (
    email_id integer NOT NULL,
    email_data date NOT NULL,
    email_dest character varying(50) NOT NULL,
    email_assunto character varying(40),
    email_texto text NOT NULL
);


ALTER TABLE public.email OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 16893)
-- Name: email_email_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.email_email_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.email_email_id_seq OWNER TO postgres;

--
-- TOC entry 4549 (class 0 OID 0)
-- Dependencies: 235
-- Name: email_email_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.email_email_id_seq OWNED BY public.email.email_id;


--
-- TOC entry 223 (class 1259 OID 16752)
-- Name: evento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.evento (
    evento_id integer NOT NULL,
    evento_nome character varying(30),
    evento_desc character varying(30),
    evento_status character varying(10),
    evento_data date,
    evento_total numeric
);


ALTER TABLE public.evento OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16751)
-- Name: evento_evento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.evento_evento_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.evento_evento_id_seq OWNER TO postgres;

--
-- TOC entry 4550 (class 0 OID 0)
-- Dependencies: 222
-- Name: evento_evento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.evento_evento_id_seq OWNED BY public.evento.evento_id;


--
-- TOC entry 231 (class 1259 OID 16853)
-- Name: movimentacaobancaria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.movimentacaobancaria (
    movbanc_id integer NOT NULL,
    movbanc_total numeric,
    movbanc_data date NOT NULL,
    usuario_user_id integer NOT NULL,
    contabancaria_contab_id integer NOT NULL
);


ALTER TABLE public.movimentacaobancaria OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 16852)
-- Name: movimentacaobancaria_movbanc_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.movimentacaobancaria_movbanc_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.movimentacaobancaria_movbanc_id_seq OWNER TO postgres;

--
-- TOC entry 4551 (class 0 OID 0)
-- Dependencies: 230
-- Name: movimentacaobancaria_movbanc_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.movimentacaobancaria_movbanc_id_seq OWNED BY public.movimentacaobancaria.movbanc_id;


--
-- TOC entry 215 (class 1259 OID 16491)
-- Name: parametrizacao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parametrizacao (
    id integer NOT NULL,
    nome_empresa character varying(100) NOT NULL,
    cnpj numeric(14,0) NOT NULL,
    logradouro character varying(100),
    numero integer,
    bairro character varying(50),
    cidade character varying(50),
    estado character(2),
    cep numeric(8,0),
    telefone numeric(11,0),
    email character varying(100)
);


ALTER TABLE public.parametrizacao OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 16490)
-- Name: parametrizacao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.parametrizacao_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.parametrizacao_id_seq OWNER TO postgres;

--
-- TOC entry 4552 (class 0 OID 0)
-- Dependencies: 214
-- Name: parametrizacao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.parametrizacao_id_seq OWNED BY public.parametrizacao.id;


--
-- TOC entry 227 (class 1259 OID 16799)
-- Name: receita; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.receita (
    receita_id integer NOT NULL,
    receita_val numeric NOT NULL,
    receita_dt_venc date NOT NULL,
    receita_lancamento date NOT NULL,
    receita_pagamento numeric,
    receita_desc character varying(100) NOT NULL,
    receita_statusconci character varying(20),
    categoriarec_catresc_id integer NOT NULL,
    usuario_user_id integer NOT NULL,
    evento_evento_id integer NOT NULL
);


ALTER TABLE public.receita OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16798)
-- Name: receita_receita_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.receita_receita_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.receita_receita_id_seq OWNER TO postgres;

--
-- TOC entry 4553 (class 0 OID 0)
-- Dependencies: 226
-- Name: receita_receita_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.receita_receita_id_seq OWNED BY public.receita.receita_id;


--
-- TOC entry 217 (class 1259 OID 16586)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    id integer NOT NULL,
    nome character varying(50) NOT NULL,
    login character varying(20) NOT NULL,
    senha character varying(100) NOT NULL,
    ativo boolean DEFAULT true,
    nivel_acesso character varying(20),
    cpf character varying(14) NOT NULL,
    telefone character varying(20),
    CONSTRAINT usuario_nivel_acesso_check CHECK (((nivel_acesso)::text = ANY ((ARRAY['ADMIN'::character varying, 'USER'::character varying, 'GUEST'::character varying])::text[])))
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16585)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuario_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuario_id_seq OWNER TO postgres;

--
-- TOC entry 4554 (class 0 OID 0)
-- Dependencies: 216
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuario_id_seq OWNED BY public.usuario.id;


--
-- TOC entry 234 (class 1259 OID 16878)
-- Name: volun_even; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.volun_even (
    evento_evento_id integer NOT NULL,
    voluntario_volu_id integer NOT NULL
);


ALTER TABLE public.volun_even OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 16872)
-- Name: voluntario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.voluntario (
    volu_id integer NOT NULL,
    volu_nome character varying(35) NOT NULL,
    volu_cell integer,
    volu_email character varying(40) NOT NULL,
    volu_logradouro character varying(40),
    volu_bairro character varying(20),
    volu_comp character varying(30),
    volu_cep integer
);


ALTER TABLE public.voluntario OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 16871)
-- Name: voluntario_volu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.voluntario_volu_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.voluntario_volu_id_seq OWNER TO postgres;

--
-- TOC entry 4555 (class 0 OID 0)
-- Dependencies: 232
-- Name: voluntario_volu_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.voluntario_volu_id_seq OWNED BY public.voluntario.volu_id;


--
-- TOC entry 4327 (class 2604 OID 16692)
-- Name: categoriadesp catdesp_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoriadesp ALTER COLUMN catdesp_id SET DEFAULT nextval('public.categoriadesp_catdesp_id_seq'::regclass);


--
-- TOC entry 4328 (class 2604 OID 16699)
-- Name: categoriarec catresc_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoriarec ALTER COLUMN catresc_id SET DEFAULT nextval('public.categoriarec_catresc_id_seq'::regclass);


--
-- TOC entry 4332 (class 2604 OID 16849)
-- Name: contabancaria contab_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contabancaria ALTER COLUMN contab_id SET DEFAULT nextval('public.contabancaria_contab_id_seq'::regclass);


--
-- TOC entry 4330 (class 2604 OID 16764)
-- Name: despesa despesa_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.despesa ALTER COLUMN despesa_id SET DEFAULT nextval('public.despesa_despesa_id_seq'::regclass);


--
-- TOC entry 4335 (class 2604 OID 16897)
-- Name: email email_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email ALTER COLUMN email_id SET DEFAULT nextval('public.email_email_id_seq'::regclass);


--
-- TOC entry 4329 (class 2604 OID 16755)
-- Name: evento evento_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.evento ALTER COLUMN evento_id SET DEFAULT nextval('public.evento_evento_id_seq'::regclass);


--
-- TOC entry 4333 (class 2604 OID 16856)
-- Name: movimentacaobancaria movbanc_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimentacaobancaria ALTER COLUMN movbanc_id SET DEFAULT nextval('public.movimentacaobancaria_movbanc_id_seq'::regclass);


--
-- TOC entry 4324 (class 2604 OID 16494)
-- Name: parametrizacao id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parametrizacao ALTER COLUMN id SET DEFAULT nextval('public.parametrizacao_id_seq'::regclass);


--
-- TOC entry 4331 (class 2604 OID 16802)
-- Name: receita receita_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receita ALTER COLUMN receita_id SET DEFAULT nextval('public.receita_receita_id_seq'::regclass);


--
-- TOC entry 4325 (class 2604 OID 16589)
-- Name: usuario id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario ALTER COLUMN id SET DEFAULT nextval('public.usuario_id_seq'::regclass);


--
-- TOC entry 4334 (class 2604 OID 16875)
-- Name: voluntario volu_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voluntario ALTER COLUMN volu_id SET DEFAULT nextval('public.voluntario_volu_id_seq'::regclass);


--
-- TOC entry 4522 (class 0 OID 16689)
-- Dependencies: 219
-- Data for Name: categoriadesp; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4524 (class 0 OID 16696)
-- Dependencies: 221
-- Data for Name: categoriarec; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4532 (class 0 OID 16846)
-- Dependencies: 229
-- Data for Name: contabancaria; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4528 (class 0 OID 16761)
-- Dependencies: 225
-- Data for Name: despesa; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4539 (class 0 OID 16894)
-- Dependencies: 236
-- Data for Name: email; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4526 (class 0 OID 16752)
-- Dependencies: 223
-- Data for Name: evento; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4534 (class 0 OID 16853)
-- Dependencies: 231
-- Data for Name: movimentacaobancaria; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4518 (class 0 OID 16491)
-- Dependencies: 215
-- Data for Name: parametrizacao; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4530 (class 0 OID 16799)
-- Dependencies: 227
-- Data for Name: receita; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4520 (class 0 OID 16586)
-- Dependencies: 217
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.usuario (id, nome, login, senha, ativo, nivel_acesso, cpf, telefone) VALUES (1, 'admin', 'admin', '123', true, 'ADMIN', '154.355.345-53', '(12) 31414-1231');


--
-- TOC entry 4537 (class 0 OID 16878)
-- Dependencies: 234
-- Data for Name: volun_even; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4536 (class 0 OID 16872)
-- Dependencies: 233
-- Data for Name: voluntario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4556 (class 0 OID 0)
-- Dependencies: 218
-- Name: categoriadesp_catdesp_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categoriadesp_catdesp_id_seq', 1, false);


--
-- TOC entry 4557 (class 0 OID 0)
-- Dependencies: 220
-- Name: categoriarec_catresc_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categoriarec_catresc_id_seq', 1, false);


--
-- TOC entry 4558 (class 0 OID 0)
-- Dependencies: 228
-- Name: contabancaria_contab_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contabancaria_contab_id_seq', 1, false);


--
-- TOC entry 4559 (class 0 OID 0)
-- Dependencies: 224
-- Name: despesa_despesa_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.despesa_despesa_id_seq', 1, false);


--
-- TOC entry 4560 (class 0 OID 0)
-- Dependencies: 235
-- Name: email_email_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.email_email_id_seq', 1, false);


--
-- TOC entry 4561 (class 0 OID 0)
-- Dependencies: 222
-- Name: evento_evento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.evento_evento_id_seq', 1, false);


--
-- TOC entry 4562 (class 0 OID 0)
-- Dependencies: 230
-- Name: movimentacaobancaria_movbanc_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.movimentacaobancaria_movbanc_id_seq', 1, false);


--
-- TOC entry 4563 (class 0 OID 0)
-- Dependencies: 214
-- Name: parametrizacao_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.parametrizacao_id_seq', 2, true);


--
-- TOC entry 4564 (class 0 OID 0)
-- Dependencies: 226
-- Name: receita_receita_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.receita_receita_id_seq', 1, false);


--
-- TOC entry 4565 (class 0 OID 0)
-- Dependencies: 216
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuario_id_seq', 1, true);


--
-- TOC entry 4566 (class 0 OID 0)
-- Dependencies: 232
-- Name: voluntario_volu_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.voluntario_volu_id_seq', 1, false);


--
-- TOC entry 4346 (class 2606 OID 16694)
-- Name: categoriadesp categoriadesp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoriadesp
    ADD CONSTRAINT categoriadesp_pkey PRIMARY KEY (catdesp_id);


--
-- TOC entry 4348 (class 2606 OID 16701)
-- Name: categoriarec categoriarec_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoriarec
    ADD CONSTRAINT categoriarec_pkey PRIMARY KEY (catresc_id);


--
-- TOC entry 4356 (class 2606 OID 16851)
-- Name: contabancaria contabancaria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contabancaria
    ADD CONSTRAINT contabancaria_pkey PRIMARY KEY (contab_id);


--
-- TOC entry 4352 (class 2606 OID 16768)
-- Name: despesa despesa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT despesa_pkey PRIMARY KEY (despesa_id);


--
-- TOC entry 4364 (class 2606 OID 16901)
-- Name: email email_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email
    ADD CONSTRAINT email_pkey PRIMARY KEY (email_id);


--
-- TOC entry 4350 (class 2606 OID 16759)
-- Name: evento evento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.evento
    ADD CONSTRAINT evento_pkey PRIMARY KEY (evento_id);


--
-- TOC entry 4358 (class 2606 OID 16860)
-- Name: movimentacaobancaria movimentacaobancaria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimentacaobancaria
    ADD CONSTRAINT movimentacaobancaria_pkey PRIMARY KEY (movbanc_id);


--
-- TOC entry 4338 (class 2606 OID 16496)
-- Name: parametrizacao parametrizacao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parametrizacao
    ADD CONSTRAINT parametrizacao_pkey PRIMARY KEY (id);


--
-- TOC entry 4354 (class 2606 OID 16806)
-- Name: receita receita_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receita
    ADD CONSTRAINT receita_pkey PRIMARY KEY (receita_id);


--
-- TOC entry 4340 (class 2606 OID 16597)
-- Name: usuario usuario_cpf_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_cpf_key UNIQUE (cpf);


--
-- TOC entry 4342 (class 2606 OID 16595)
-- Name: usuario usuario_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_login_key UNIQUE (login);


--
-- TOC entry 4344 (class 2606 OID 16593)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 4362 (class 2606 OID 16882)
-- Name: volun_even volun_even_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.volun_even
    ADD CONSTRAINT volun_even_pkey PRIMARY KEY (evento_evento_id, voluntario_volu_id);


--
-- TOC entry 4360 (class 2606 OID 16877)
-- Name: voluntario voluntario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voluntario
    ADD CONSTRAINT voluntario_pkey PRIMARY KEY (volu_id);


--
-- TOC entry 4365 (class 2606 OID 16769)
-- Name: despesa despesa_categoriadesp_catdesp_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT despesa_categoriadesp_catdesp_id_fkey FOREIGN KEY (categoriadesp_catdesp_id) REFERENCES public.categoriadesp(catdesp_id);


--
-- TOC entry 4366 (class 2606 OID 16779)
-- Name: despesa despesa_evento_evento_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT despesa_evento_evento_id_fkey FOREIGN KEY (evento_evento_id) REFERENCES public.evento(evento_id);


--
-- TOC entry 4367 (class 2606 OID 16774)
-- Name: despesa despesa_usuario_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT despesa_usuario_user_id_fkey FOREIGN KEY (usuario_user_id) REFERENCES public.usuario(id);


--
-- TOC entry 4371 (class 2606 OID 16866)
-- Name: movimentacaobancaria movimentacaobancaria_contabancaria_contab_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimentacaobancaria
    ADD CONSTRAINT movimentacaobancaria_contabancaria_contab_id_fkey FOREIGN KEY (contabancaria_contab_id) REFERENCES public.contabancaria(contab_id);


--
-- TOC entry 4372 (class 2606 OID 16861)
-- Name: movimentacaobancaria movimentacaobancaria_usuario_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimentacaobancaria
    ADD CONSTRAINT movimentacaobancaria_usuario_user_id_fkey FOREIGN KEY (usuario_user_id) REFERENCES public.usuario(id);


--
-- TOC entry 4368 (class 2606 OID 16807)
-- Name: receita receita_categoriarec_catresc_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receita
    ADD CONSTRAINT receita_categoriarec_catresc_id_fkey FOREIGN KEY (categoriarec_catresc_id) REFERENCES public.categoriarec(catresc_id);


--
-- TOC entry 4369 (class 2606 OID 16817)
-- Name: receita receita_evento_evento_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receita
    ADD CONSTRAINT receita_evento_evento_id_fkey FOREIGN KEY (evento_evento_id) REFERENCES public.evento(evento_id);


--
-- TOC entry 4370 (class 2606 OID 16812)
-- Name: receita receita_usuario_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receita
    ADD CONSTRAINT receita_usuario_user_id_fkey FOREIGN KEY (usuario_user_id) REFERENCES public.usuario(id);


--
-- TOC entry 4373 (class 2606 OID 16883)
-- Name: volun_even volun_even_evento_evento_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.volun_even
    ADD CONSTRAINT volun_even_evento_evento_id_fkey FOREIGN KEY (evento_evento_id) REFERENCES public.evento(evento_id);


--
-- TOC entry 4374 (class 2606 OID 16888)
-- Name: volun_even volun_even_voluntario_volu_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.volun_even
    ADD CONSTRAINT volun_even_voluntario_volu_id_fkey FOREIGN KEY (voluntario_volu_id) REFERENCES public.voluntario(volu_id);


ALTER TABLE parametrizacao
    ADD COLUMN complemento VARCHAR(255);

-- Completed on 2025-05-16 14:23:47 -03

--
-- PostgreSQL database dump complete
--

