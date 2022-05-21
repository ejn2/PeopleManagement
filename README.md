# People management
É uma api simples para mostar como implementar o JWT (Json Web Token), no Java - Spring Boot

## Rotas

Use a url abaixo para acessar as rotas.

### Exemplo:
<code>http://localhost:8080/api/</code>

<br>

<table border="1">
    <th>Endpoint</th><th>Método</th><th>Ação</th>
    <tr>
        <td>/api/login</td><td>POST</td><td>Obtém o token de acesso
</td>
    </tr>
        <tr>
        <td>/api/user</td><td>POST</td><td>Registra um novo usuário.
</td>
    </tr>
        <tr>
        <td>/api/user</td><td>GET</td><td>Mostra uma lista de todos os usuários. (É necessário ter o token de acesso administrativo)
</td>
</td>
    </tr>
        <tr>
        <td>/api/user/{id}</td><td>GET</td><td>Mostra um único usuário. (É necessário ter o token de acesso administrativo)
</td>
</td>
    </tr>
    </td>
    </tr>
        <tr>
        <td>/api/user/{id}</td><td>DELETE</td><td>Exclui um usuário. (É necessário ter o token de acesso administrativo)
</td>
</td>
    </tr>
    </td>
    </tr>
        <tr>
        <td>/api/admin/{id}</td><td>PATCH</td><td> Adicionar novo role ao usuário. (É necessário ter o token de acesso administrativo)

</td>
    </tr>
</table>

<br>

## Dados de entrada.

⚠️ Todos os dados de entrada devem ser passados no formato JSON.

### Exemplo:

#### Registrando novo usuario.
- Endpoint <code>/api/user</code> Metodo: <code>[POST]</code>

~~~JSON
{
  "name": "User-001",
  "username": "user001",
  "password": "12345678"
}
~~~

#### Executando o Login.
Para obter o <code>access token</code> (token de acesso), você deve informar <code>username</code> e <code>password</code>

- Endpoint <code>/api/login</code> Metodo: <code>[POST]</code>

~~~JSON
{
  "username": "user001",
  "password": "12345678"
}
~~~

Logo após o request ter sido executado, você receberá um token de acesso, parecido com o do exemplo abaixo.

~~~JSON
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6InVzZXIwMDEiLCJpYXQiOjE1MTYyMzkwMjJ9.uMpxKaednRxR8uPP7QwqjzfHL8PmEl0cq3Z6bh9RadM",
}
~~~

#### Acessando recursos.

Para acessar os recursos você vai precisar ter um token de acesso, como já foi mostrado acima. O token deve ser passado nas <code>headers</code> da requisição. Veja o exemplo abaixo.

<table>
    <tr>
        <td>Authorization</td>
        <td>Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6InVzZXIwMDEiLCJpYXQiOjE1MTYyMzkwMjJ9.uMpxKaednRxR8uPP7QwqjzfHL8PmEl0cq3Z6bh9RadM</td>
    </tr>
</table>

Pode ser notado que um novo “elemento” foi acrescentado nas <code>heades</code>, chamado <code>Bearer</code>
<br>
[Saber mais sobre headers.](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Headers)


#### Concedendo novas permissões

Para conceder novas permissões para o usuário, o usuário atual precisa ter permissões administrativas (admin role). Veja o exemplo abaixo.

- Endpoint <code>/api/admin/1</code> Metodo: <code>[PATCH]</code>
~~~JSON
    {
        "name": "ADMIN"
    }
~~~

O <code>id</code> deve ser passado como parâmetro na url, e o nome da <code>role</code> (Deve ser uma role já registrada no banco de dados, caso contrário, uma exceção será lançada. RoleNotFoundException) deve ser passado no corpo da requisição no formato JSON.
